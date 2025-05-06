package com.example.demo.io.Netty;

import com.example.demo.io.Netty.utils.MyWriter;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.concurrent.TimeUnit;

/**
 * 可以连接 NIO，保持通信
 * 可以链接websocket  BUT: 虽然不报错，但是不能收发消息
 */
public class NettyClientSSL {

    public static final String rsa = "/Users/yz/asiainnovations/personal-master-client/src/main/java/com/example/demo/io/Netty/jkl/";

    @SneakyThrows
    public static void main(String[] args) {
        SSLContext sslContext = getClientContext();

        //客户端需要一个事件循环组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是ServerBootstrap而是Bootstrap
            Bootstrap bootstrap = new Bootstrap();
            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) // 使用NioSocketChannel作为客户端的通道实现
                    .handler(new MyClientChannelInitializer(sslContext));

            System.out.println("netty client start。。");
            //启动客户端去连接服务器端
            ChannelFuture cf = bootstrap.connect("127.0.0.1", NettyServer.port).sync();
            // 写入器
//            new Thread(new MyWriter(cf.channel())).start();
            //对通道关闭进行监听
            cf.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    static class MyClientChannelInitializer extends ChannelInitializer<SocketChannel> {

        private SSLContext sslContext;

        public MyClientChannelInitializer(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            SSLEngine sslEngine = sslContext.createSSLEngine();
            // 用来设置客户模式或者服务器模式。true表示客户模式，即无需向对方证实自己的身份；false表示服务器模式，即需要向对方证实自己的身份。
            sslEngine.setUseClientMode(true);
            //是否需要验证客户端 。 如果是双向认证，则需要将其设置为true，同时将client证书添加到server的信任列表中
            sslEngine.setNeedClientAuth(true);
            ch.pipeline().addLast("ssl", new SslHandler(sslEngine));
            // 处理器都要在 ssl之后
            ch.pipeline().addLast(new ClientHeartbeatHandler());
            ch.pipeline().addLast(new NettyClientHandler());

        }
    }


    static class NettyClientHandler extends ChannelInboundHandlerAdapter {

        /**
         * 当客户端连接服务器完成就会触发该方法
         *
         * @param ctx
         * @throws Exception
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            ByteBuf buf = Unpooled.copiedBuffer("HelloServer".getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(buf);
        }

        //当通道有读取事件时会触发，即服务端发送数据给客户端
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object obj) {
            ByteBuf buf = (ByteBuf) obj;
            String msg = buf.toString(CharsetUtil.UTF_8);
            if (!msg.equals("pang")) {
                System.out.println("收到服务端的消息:" + buf.toString(CharsetUtil.UTF_8));
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }

    private static final String PROTOCOL = "TLS";

    private static SSLContext sslContext;

    @SneakyThrows
    public static SSLContext getClientContext(){
        if(sslContext !=null) return sslContext;
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        KeyStore tks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(rsa + "kclient.ks"), "clientpass".toCharArray());
        tks.load(new FileInputStream(rsa + "tclient.ks"), "clientpublicpass".toCharArray());
        kmf.init(ks, "clientpass".toCharArray());
        tmf.init(tks);
        sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }

    static class ClientHeartbeatHandler extends IdleStateHandler {

        // 设置写事件为5s
        // 如果5s没有写事件发生 就会触发下面的IdleStateEvent
        private static final int WRITE_IDLE_TIME = 3;

        public ClientHeartbeatHandler() {
            super(0, WRITE_IDLE_TIME, 0, TimeUnit.SECONDS);
        }

        @Override
        protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
            // 指定时间内没有写事件发送 就会触发 IdleState.WRITER_IDLE 类型事件
            // 我们就可以对该连接进行处理 主动发送心跳
            if(evt.state()== IdleState.WRITER_IDLE){
                ctx.writeAndFlush(Unpooled.copiedBuffer("ping".getBytes(CharsetUtil.UTF_8)));
            }
        }
    }

}
