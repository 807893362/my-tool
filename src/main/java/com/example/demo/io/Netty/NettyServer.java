package com.example.demo.io.Netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.SneakyThrows;

public class NettyServer {

    public static final int port = 9000;


    @SneakyThrows
    public static void main(String[] args) {
        // 创建两个线程组bossGroup和workerGroup, 含有的子线程NioEventLoop的个数默认为cpu核数的两倍
        // bossGroup只是处理连接请求 ,真正的和客户端业务处理，会交给workerGroup完成
        EventLoopGroup bossGroup = new NioEventLoopGroup(3);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);
        try {
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            // 使用链式编程来配置参数
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    // 使用NioServerSocketChannel作为服务器的通道实现
                    .channel(NioServerSocketChannel.class)
                    // 初始化服务器连接队列大小，服务端处理客户端连接请求是顺序处理的,所以同一时间只能处理一个客户端连接。
                    // 多个客户端同时来的时候,服务端将不能处理的客户端连接请求放在队列中等待处理
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建通道初始化对象，设置初始化参数，在 SocketChannel 建立起来之前执行
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //对workerGroup的SocketChannel设置处理器
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    })
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

    /**
     * 自定义Handler需要继承netty规定好的某个HandlerAdapter(规范)
     */
    static class NettyServerHandler extends ChannelInboundHandlerAdapter {

        /**
         * 当客户端连接服务器完成就会触发该方法
         */
        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            System.out.println("客户端连接通道建立完成");
        }

        /**
         * 读取客户端发送的数据
         */
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            //Channel channel = ctx.channel();
            //ChannelPipeline pipeline = ctx.pipeline(); //本质是一个双向链接, 出站入站
            //将 msg 转成一个 ByteBuf，类似NIO 的 ByteBuffer
            ByteBuf buf = (ByteBuf) msg;
            System.out.println("收到客户端的消息:" + buf.toString(CharsetUtil.UTF_8));
        }

        /**
         * 数据读取完毕处理方法
         */
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ByteBuf buf = Unpooled.copiedBuffer("HelloClient".getBytes(CharsetUtil.UTF_8));
            ctx.writeAndFlush(buf);
        }

        /**
         * 处理异常, 一般是需要关闭通道
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    }


}
