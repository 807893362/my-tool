package com.example.demo.io.Netty;

import com.example.demo.io.Netty.utils.Packet;
import com.example.demo.io.Netty.utils.PacketCodeC;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * - 鉴权HttpRequestHandler
 * - WebSocketServerProtocolHandler
 * -- 设置链接参数
 * [O] SSL
 * [X] Netty client - 没有对应的实现
 */
public class NettyWSServerPlus {

    public static final int port = 9000;

    // 服务端开启，客户端必须加载自己的证书等待验证客户端，否则客户端无法连接
    // 服务端未开启，客户端启不启用证书都不影响握手
    private static final boolean SSL_BOTHWAY = false;

    /**
     * ws://127.0.0.1:9000/token1
     * test => https://try8.cn/tool/zone/websocket
     */
    @SneakyThrows
    public static void main(String[] args) {
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();    //主线程池
        NioEventLoopGroup minorGrp = new NioEventLoopGroup();     //从线程池
        try {
            SSLContext sslContext = getServerContext();

            ServerBootstrap serverBootstrap = new ServerBootstrap(); //创建Netty服务器启动对象
            serverBootstrap
                    .group(mainGrp, minorGrp)  //指定上面创建的两个线程池
                    .channel(NioServerSocketChannel.class)   //指定通道类型
                    .childHandler(new WebSocketChannelInitializer(sslContext));  //指定初始化器用来加载Channel收到事件消息

            ChannelFuture future = serverBootstrap.bind(port).sync();   //服务器绑定端口，启动服务，同步
            System.err.format("server start port: %s \n", port);

            future.channel().closeFuture().sync(); //等待服务器关闭

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            mainGrp.shutdownGracefully();
            minorGrp.shutdownGracefully();
        }
    }

    static class WebSocketChannelInitializer extends ChannelInitializer<SocketChannel> {

        private SSLContext sslContext;

        public WebSocketChannelInitializer(SSLContext sslContext) {
            this.sslContext = sslContext;
        }

        protected void initChannel(SocketChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();
            //设置为服务器模式
            SSLEngine sslEngine = sslContext.createSSLEngine();
            sslEngine.setUseClientMode(false);
            //是否需要验证客户端 。 如果是双向认证，则需要将其设置为true，同时将client证书添加到server的信任列表中
            sslEngine.setNeedClientAuth(SSL_BOTHWAY);
            pipeline.addLast("ssl", new SslHandler(sslEngine));
            /**
             * 在http协议中netty使用HttpServerCodec，进行编解码，主要这个包含编码和界面，内部继承了两个类一个编码类，一个解码类
             */
            pipeline.addLast(new HttpServerCodec());
            /**
             * 添加一个大数据流的支持
             * 是以块方式写，添加ChunkedWriteHandler处理器
             */
            pipeline.addLast(new ChunkedWriteHandler());
            /**
             * 捕捉出站异常 ChannelOutboundHandlerAdapter
             */
            pipeline.addLast(new OutBoundExceptionHandler());
            /**
             * 主要作用当浏览器发送大量数据时，就会发出多次http请求，就是为了让其聚合处理消息
             * 主要将HTTPMessage聚合成FullhttpRequest/Response
             */
            pipeline.addLast(new HttpObjectAggregator(1024 * 64));
            /**
             * WebSocket 数据压缩扩展
             */
            pipeline.addLast(new WebSocketServerCompressionHandler());
            /**
             * 自定义uri handler
             * 鉴权 & uri 匹配 WebSocketServerProtocolHandler->websocketPath
             * 握手完成后可以remove,提升性能
             */
            pipeline.addLast(new HttpRequestHandler());
            /**
             * 协议处理器：主要作用，就是将其返回的状态码变成101、将其http协议升级为ws（webSocket）协议、握手、控制帧处理
             * websocketPath: uri 必须完整匹配/ws
             * - ws://127.0.0.1:9000/ws/ssdsds || ws://127.0.0.1:9000/ssdsds/ws 均无法访问
             * subprotocols: 子协议
             * allowExtensions: 是否允许扩展
             */
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws", null, true));
            /* 编解码*/
            pipeline.addLast(new PacketCodeC.WSDecoder());
            pipeline.addLast(new PacketCodeC.WSEncoder());
            /**
             * 心跳机制：读空闲35秒，超过10秒时间客户端未向channel写入数据触发
             */
            pipeline.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
            /**
             * 自定义 收发器
             */
            pipeline.addLast(new ChatHandler());
        }
    }

    static class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
        public static AttributeKey<String> TOKEN = AttributeKey.valueOf("token");

        @Override
        public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {

            String url = request.uri();
            System.err.println("HttpRequestHandler channelRead0 url=>" + url);

            if (-1 != url.indexOf("/")) {

                String temp[] = url.split("/");
                if (temp.length >= 2) {
                    String token = URLDecoder.decode(temp[1], "UTF-8");
                    ctx.channel().attr(TOKEN).set(token);
                }
                request.setUri("/ws");
                // 传递到下一个handler：升级握手
                ctx.fireChannelRead(request.retain());
            } else {
                System.err.println("HttpRequestHandler 链接不合法 close");
                ctx.close();
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            ctx.close();
        }
    }

    static class ChatHandler extends SimpleChannelInboundHandler<Packet> {

        private static final String SPLIT = ":\t";
        private static final ChannelGroup group = new DefaultChannelGroup("ChannelGroups", GlobalEventExecutor.INSTANCE);
        private static final ConcurrentHashMap<String, Channel> tokenChannel = new ConcurrentHashMap<String, Channel>();

        private static String getOnine() {
            StringBuilder stringBuilder = new StringBuilder(group.size() * 20);
            String online = "[在线用户]";
            stringBuilder.append(online);
            for(Channel channel : group) {
                String name = channel.attr(HttpRequestHandler.TOKEN).get();
                stringBuilder.append(" " + name);
            }
            return stringBuilder.toString();
        }

        // 搞清楚作用， 重写后 handlerAdded  channelRead0 失效
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            // SSL 验证-这里只是监听，并不影响SSL建联
            ctx.pipeline().get(SslHandler.class).handshakeFuture()
                    .addListener(new GenericFutureListener<Future<Channel>>() {

                        @Override
                        public void operationComplete(Future<Channel> arg0)
                                throws Exception {
                            if (arg0.isSuccess()) {
                                ctx.writeAndFlush(new Packet.TxtPacket("Welcome to " + ctx.channel().localAddress() + " secure chat service!"));
                                ctx.writeAndFlush(new Packet.TxtPacket("Your session is protected by " + ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() + " cipher suite."));
                            } else {
                                System.err.println("error " + System.currentTimeMillis() + " =>验证失败");
                            }
                        }
                    });

            String toekn = ctx.channel().attr(HttpRequestHandler.TOKEN).get();
            if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                System.out.format("新建 :%s \n", toekn);
                if(null == toekn){
                    return;
                }
                // 移除性能更佳 TODO 搞清楚为什么可以剔除？ 为什么不会影响新用户的链接？
                ctx.pipeline().remove(HttpRequestHandler.class);
                Channel old = tokenChannel.put(toekn, ctx.channel());
                if (old != null) {
                    group.remove(old);
                    System.out.println(old.attr(HttpRequestHandler.TOKEN).get() + SPLIT + "［切换设备］");
                    ChannelFuture channelFuture = old.writeAndFlush("您的账户在其它地方登陆");
                    channelFuture.addListener(ChannelFutureListener.CLOSE);
                }
                ctx.writeAndFlush("－＝＝＝＝＝登录成功＝＝＝＝＝－");
                String up = toekn + SPLIT + "［上线］";
                System.out.println(up);
                group.writeAndFlush(new Packet.TxtPacket(up));

                group.add(ctx.channel());

                String online = getOnine();
                System.out.println(online);
                group.writeAndFlush(new Packet.TxtPacket(online));
            } else if (evt instanceof IdleStateEvent) {
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
        public void channelInactive(ChannelHandlerContext ctx) {
            String toekn = ctx.channel().attr(HttpRequestHandler.TOKEN).get();
            if(null == toekn){
                return;
            }
            System.out.format("channelInactive 退出 %s \n", toekn);
            tokenChannel.remove(toekn);
            String down = toekn + SPLIT + "［下线］";
            System.out.println(down);
            group.writeAndFlush(new Packet.TxtPacket(down));
            group.remove(ctx.channel());

            String online = getOnine();
            System.out.println(online);
            group.writeAndFlush(new Packet.TxtPacket(online));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, Packet packet) throws Exception {
            String token = ctx.channel().attr(HttpRequestHandler.TOKEN).get();
            if(packet instanceof Packet.HeartPacket) {
            } else if(packet instanceof Packet.TxtPacket) {
                String msg = ((Packet.TxtPacket) packet).getTxt();
                System.err.println("收到客户端消息:" + msg);
                // 聊天发给所有人
                String send = token + SPLIT + msg;
                group.writeAndFlush(new Packet.TxtPacket(send));
            } else {
                System.err.println("无法识别的消息类型" + packet);
            }
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            System.out.println("异常发生");
            ctx.close();
        }

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

    private static final String PROTOCOL = "TLS";
    private static SSLContext sslContext;

    public static final String rsa = "/data/project-pri/personal-master-client/src/main/java/com/example/demo/io/Netty/jkl/";

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

}
