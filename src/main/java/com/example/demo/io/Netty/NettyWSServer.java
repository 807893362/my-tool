package com.example.demo.io.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.AttributeKey;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * - 鉴权HttpRequestHandler
 * - WebSocketServerProtocolHandler
 * -- 设置链接参数
 *
 */
public class NettyWSServer {

    public static final int port = 9000;

    /**
     * ws://127.0.0.1:9000/token1
     * test => https://try8.cn/tool/zone/websocket
     */
    @SneakyThrows
    public static void main(String[] args) {
        NioEventLoopGroup mainGrp = new NioEventLoopGroup();    //主线程池
        NioEventLoopGroup minorGrp = new NioEventLoopGroup();     //从线程池
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap(); //创建Netty服务器启动对象
            serverBootstrap
                    .group(mainGrp, minorGrp)  //指定上面创建的两个线程池
                    .channel(NioServerSocketChannel.class)   //指定通道类型
                    .childHandler(new WebSocketChannelInitializer());  //指定初始化器用来加载Channel收到事件消息

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
        protected void initChannel(SocketChannel socketChannel) {
            ChannelPipeline pipeline = socketChannel.pipeline();
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
            /**
             * string -> websocketframe
             */
            pipeline.addLast(new WebSocketFrameEncoder());
            /**
             * websocketframe -> string
             */
            pipeline.addLast(new WebSocketFrameDecoder());
            /**
             * 心跳机制：读空闲35秒，超过10秒时间客户端未向channel写入数据触发
             */
            pipeline.addLast(new IdleStateHandler(5, 0, 0, TimeUnit.SECONDS));
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

    static class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

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
                String up = toekn + SPLIT + "［上线］-" + System.currentTimeMillis()/1000;
                System.out.println(up);
                group.writeAndFlush(new TextWebSocketFrame(up));

                group.add(ctx.channel());

                String online = getOnine();
                System.out.println(online);
                group.writeAndFlush(new TextWebSocketFrame(online));
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
            String down = toekn + SPLIT + "［下线］-" + System.currentTimeMillis()/1000;;
            System.out.println(down);
            group.writeAndFlush(new TextWebSocketFrame(down));
            group.remove(ctx.channel());

            String online = getOnine();
            System.out.println(online);
            group.writeAndFlush(new TextWebSocketFrame(online));
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) throws Exception {
            String token = ctx.channel().attr(HttpRequestHandler.TOKEN).get();
            String msg = frame.text();
            System.err.format("ChatHandler channelRead0 %s-%s \n", token, msg);
            if("ping".equals(msg)) {
                // 心跳只给自己发
                ctx.channel().writeAndFlush(new TextWebSocketFrame("pang"));
            } else {
                // 聊天发给所有人
                String send = token + SPLIT + msg;
                group.writeAndFlush(new TextWebSocketFrame(send));
                // group.writeAndFlush 等价与下面
//                tokenChannel.values().stream().parallel().forEach(channel -> {
//                    channel.writeAndFlush(new TextWebSocketFrame(send));
//                });
            }
        }

       /* 不能设置，会打乱handler 执行顺序
        @Override
        public void handlerAdded(ChannelHandlerContext ctx) {
            System.out.format("新建 :%s \n", "toekn");
        }
        @Override
        public void handlerRemoved(ChannelHandlerContext ctx) {
            System.out.format("退出 :%s \n", "toekn");
        }*/

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            System.out.println("异常发生");
            ctx.close();
        }

    }

    static class WebSocketFrameDecoder extends MessageToMessageDecoder<TextWebSocketFrame> {

        @Override
        protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
            String text = msg.text();
            out.add(new TextWebSocketFrame(text + ",decoder"));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    static class WebSocketFrameEncoder extends MessageToMessageEncoder<TextWebSocketFrame> {

        @Override
        protected void encode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) throws Exception {
//            String text = msg.text();
//            out.add(new TextWebSocketFrame(Base64.getEncoder().encodeToString(text.getBytes())));
            out.add(new TextWebSocketFrame(msg.text()));
        }
    }

    static class OutBoundExceptionHandler extends ChannelOutboundHandlerAdapter {
        @Override
        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            System.err.println("OutBoundExceptionHandler write");
            super.write(ctx, msg, promise);
        }

        @Override
        public void read(ChannelHandlerContext ctx) throws Exception {
            System.err.println("OutBoundExceptionHandler read");
            super.read(ctx);
        }
    }

}
