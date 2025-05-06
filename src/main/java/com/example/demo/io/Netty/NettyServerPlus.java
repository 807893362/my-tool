package com.example.demo.io.Netty;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.demo.io.Netty.utils.Packet;
import com.example.demo.io.Netty.utils.PacketCodeC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatchers;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 完善：
 * [O]鉴权
 * [O]login & token
 * [O]数据类型封装
 * [O]加解密
 * [O]心跳
 * [O]群聊
 * [O]netty优化
 * [O]SSL 封装单双验证
 */
public class NettyServerPlus {

    public static final int port = 9000;

    public static final String rsa = "/data/project-pri/personal-master-client/src/main/java/com/example/demo/io/Netty/jkl/";

    // 服务端开启，客户端必须加载自己的证书等待验证客户端，否则客户端无法连接
    // 服务端未开启，客户端启不启用证书都不影响握手
    private static final boolean SSL_BOTHWAY = false;

    @SneakyThrows
    public static void main(String[] args) {
        // 创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(3);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            SSLContext sslContext = getServerContext();
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            bootstrap
                /* 禁用nagle算法*/
                .childOption(ChannelOption.TCP_NODELAY, true)
                /* 用来探测客户端是否存活 时间取决的服务器配置*/
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                 /* 允许在同一端口上启动同一服务器的多个实例*/
                .childOption(ChannelOption.SO_REUSEADDR, true)
                 /* Netty4使用对象池，重用缓冲区*/
                .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
                 /* 定义接收或者传输的系统缓冲区buf的大小*/
                .childOption(ChannelOption.SO_RCVBUF, 1048576)
                /* 定义接收或者传输的系统缓冲区buf的大小*/
                .childOption(ChannelOption.SO_SNDBUF, 1048576);

            // 使用链式编程来配置参数
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    // 使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                    // 多个客户端同时来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new MyChannelInitializer(sslContext))
            ;
            System.out.println("netty server start。。");
            // 绑定一个端口并且同步, 生成了一个ChannelFuture异步对象，通过isDone()等方法可以判断异步事件的执行情况
            // 启动服务器(并绑定端口)，bind是异步操作，sync方法是等待异步操作执行完毕
            ChannelFuture cf = bootstrap.bind(port).sync();
            // 给cf注册监听器，监听我们关心的事件
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (cf.isSuccess()) {
                        System.out.println("监听端口9000成功");
                    } else {
                        System.out.println("监听端口9000失败");
                    }
                }
            });
            // 等待服务端监听端口关闭，closeFuture是异步操作
            // 通过sync方法同步等待通道关闭处理完毕，这里会阻塞等待通道关闭完成，内部调用的是Object的wait()方法
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

    static class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

        private SSLContext sslContext;

        public MyChannelInitializer(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        @Override
        protected void initChannel(SocketChannel channel) {
            //设置为服务器模式
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            //是否需要验证客户端 。 如果是双向认证，则需要将其设置为true，同时将client证书添加到server的信任列表中
            sslEngine.setNeedClientAuth(SSL_BOTHWAY);
            channel.pipeline().addLast("ssl", new SslHandler(sslEngine));
            /**
             * 捕捉出站异常 ChannelOutboundHandlerAdapter
             */
            channel.pipeline().addLast(new OutBoundExceptionHandler());
            /**
             * 解码
             */
            channel.pipeline().addLast(new PacketCodeC.Encoder());
            /**
             * 编码
             */
            channel.pipeline().addLast(new PacketCodeC.Decoder());
            /**
             * 心跳机制：读空闲35秒，超过10秒时间客户端未向channel写入数据触发
             */
            channel.pipeline().addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
            // 自定义解析数据handler
            channel.pipeline().addLast(new NettyServerHandler());
        }
    }

    /**
     * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
     */
    static class NettyServerHandler extends SimpleChannelInboundHandler<Packet> {

        private static final String SPLIT = ":\t";
        private static final ChannelGroup group = new DefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);
        private static final ConcurrentHashMap<String, Channel> tokenChannel = new ConcurrentHashMap<String, Channel>();
        public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

        private static String getOnine() {
            StringBuilder stringBuilder = new StringBuilder(group.size() * 20);
            String online = "[在线用户]";
            stringBuilder.append(online);
            for (Channel channel : group) {
                stringBuilder.append(" " + channel.attr(NettyWSServer.HttpRequestHandler.TOKEN).get());
            }
            return stringBuilder.toString();
        }

        // 超时等事件监听
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            String toekn = ctx.channel().attr(NettyWSServer.HttpRequestHandler.TOKEN).get();
            if (evt instanceof IdleStateEvent) {
                // 2*4+1 s内读空闲时，关掉连接，表示客户端不可见了
                IdleStateEvent evnet = (IdleStateEvent) evt;
                if (evnet.state().equals(IdleState.READER_IDLE)) {
                    System.out.format("链接超时 :%s \n", toekn);
                    ctx.close();
                }
            } else {
                super.userEventTriggered(ctx, evt);
            }
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
            if(packet instanceof Packet.HeartPacket){
                ctx.writeAndFlush(PacketCodeC.INSTANCE.encode(ctx.alloc(), new Packet.HeartPacket()));
            } else if(packet instanceof Packet.LoginRequestPacket){
                String token = ((Packet.LoginRequestPacket) packet).getToken();
                ctx.channel().attr(TOKEN).set(token);
                System.out.println(", token:" + token);
                Channel old = tokenChannel.put(token, ctx.channel());
                if (old != null) {
                    group.remove(old);
                    System.out.println(old.id().asShortText() + SPLIT + "［切换设备］");
                    ChannelFuture channelFuture = old.writeAndFlush(sendTxt("您的账户在其它地方登陆"));
                    channelFuture.addListener(ChannelFutureListener.CLOSE);
                }
                group.add(ctx.channel());
                String online = getOnine();
                System.out.println(online);
                group.writeAndFlush(sendTxt(online));
                return;
            } else if(packet instanceof Packet.TxtPacket){
                String txt = ((Packet.TxtPacket) packet).getTxt();
                System.out.println("收到客户端的消息:" + txt);
                // 群转发
                String send = ctx.channel().attr(NettyWSServer.HttpRequestHandler.TOKEN).get() + SPLIT + txt;
                group.writeAndFlush(sendTxt(send), ChannelMatchers.isNot(ctx.channel()));
            }
        }


        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            SocketChannel channel = (SocketChannel) ctx.channel();

            // SSL 验证-这里只是监听，并不影响SSL建联
            ctx.pipeline().get(SslHandler.class).handshakeFuture()
                    .addListener(new GenericFutureListener<Future<Channel>>() {

                        @Override
                        public void operationComplete(Future<Channel> arg0)
                                throws Exception {
                            if (arg0.isSuccess()) {
                                ctx.writeAndFlush(sendTxt("Welcome to " + channel.localAddress().getHostString() + " secure chat service!"));
                                ctx.writeAndFlush(sendTxt("Your session is protected by " + ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() + " cipher suite."));
                            } else {
                                System.err.println("SSL 验证失败");
                            }
                        }
                    });

            System.out.print(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + " conn. ");
            System.out.print("IP:" + channel.localAddress().getHostString());
            System.out.println(", Port:" + channel.localAddress().getPort());
        }

        @Override
        public void channelInactive(ChannelHandlerContext ctx) {
            String toekn = ctx.channel().attr(NettyWSServer.HttpRequestHandler.TOKEN).get();
            System.out.format("channelInactive 退出 %s \n", toekn);
            if(null == toekn){
                toekn = ctx.channel().id().asShortText();
            }
            tokenChannel.remove(toekn);
            String down = toekn + SPLIT + "［下线］";
            ;
            System.out.println(down);
            group.writeAndFlush(sendTxt(down));
            group.remove(ctx.channel());

            String online = getOnine();
            System.out.println(online);
            group.writeAndFlush(sendTxt(online));
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            System.out.println("异常发生");
            ctx.close();
        }

    }

    private static ByteBuf sendTxt(ChannelHandlerContext ctx, String txt) {
        return PacketCodeC.INSTANCE.encode(ctx.alloc(), new Packet.TxtPacket(txt));
    }

    private static Packet sendTxt(String txt) {
        return new Packet.TxtPacket(txt);
    }

    private static final String PROTOCOL = "TLS";
    private static SSLContext sslContext;

    @SneakyThrows
    public static SSLContext getServerContext() {
        if (sslContext != null) return sslContext;
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(rsa + "kserver.ks"), "serverpass".toCharArray());
        kmf.init(ks, "serverpass".toCharArray());

        // 双向验证
        TrustManagerFactory tmf = null;
        if(SSL_BOTHWAY){
            tmf = TrustManagerFactory.getInstance("SunX509");
            KeyStore tks = KeyStore.getInstance("JKS");
            tks.load(new FileInputStream(rsa + "tserver.ks"), "serverpublicpass".toCharArray());
            tmf.init(tks);
        }

        //获取安全套接字协议（TLS协议）的对象
        sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(kmf.getKeyManagers(), SSL_BOTHWAY ? tmf.getTrustManagers() : null, null);
        return sslContext;
    }

    static class OutBoundExceptionHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            super.write(ctx, msg, promise);
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            super.read(ctx);
        }
    }

}
