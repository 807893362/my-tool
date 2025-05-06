package com.example.demo.io.NIO;

import lombok.SneakyThrows;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO-多路复用
 */
public class EpollServer {

    public static final int port = 9000;

    public static void main(String[] args) {
        EpollServer epollServer = new EpollServer();
        epollServer.start();
    }

    @SneakyThrows
    private Selector initServer() {
        // 第一步：socket 建立 ServerSocketChannel : nio socket
        ServerSocketChannel server = null;
        // server 就是 socket  fd 文件
        server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));


        Selector selector = null;
        // select poll  epoll ，优先选择epoll,但是通过-DJVM参数修改
        // 如果是在epoll 模型下，open 会调用内核的epoll_create(),拿到epfd
        selector = Selector.open();

        // 如果是select\poll ，jvm 会开辟一个数组存放server  fd 文件
        // 如果是 epoll,回到用kernel epoll_crl(epfd,ADD,server fd, epollin)
        server.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }

    @SneakyThrows
    public void start() {
        Selector selector = initServer();
        // 如果是 select/poll,这里调用的是 select(fds)
        // 如果是epoll，这里调用epoll_wait()
        // 不带参数或者为0 ，表示永久阻塞，可以通过selector.wakeup()唤醒
        while (selector.select() > 0) {
            // 返回有状态的 fd 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iter = selectionKeys.iterator();

            // 上面只会返回状态变更的fd有哪些，具体的 读写 还需要自己处理
            while (iter.hasNext()) {
                SelectionKey key = iter.next();
                iter.remove();
                if (key.isAcceptable()) {
                    //如果是select/poll 会在jvm数组中添加
                    //如果是epoll 接受连接并且返回 新连接的fd，调用 epoll_crt() 添加进红黑树
                    acceptHandler(key, selector);
                } else if (key.isReadable()) {
                    readHandler(key);
                } else if (key.isWritable()) {
                    // 写事件 -- send queue只要是空的就一定返回可以写的事件
                    // 什么时候写？
                    // 第一步：准备好写什么
                    // 第二步：才开始关系 send queue 时候有空间
                    // *read 一开始就要 注册，但是write 依赖以上关系，什么时候用什么时候注册
                    // *如果一开始就注册 write ，会导致进入死循环，一直进入到该方法
                    writeHandler(key);
                }
            }
        }

    }

    @SneakyThrows
    private void acceptHandler(SelectionKey key, Selector selector) {
        ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
        // 调用accept 准备接受客户端
        SocketChannel client = ssc.accept();

        // 以下步骤 同 初始化
        client.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(8192);
        // OP_READ 关注的是 EPOLLIN（写操作）
        // buffer ,为了read 时候直接用
        client.register(selector, SelectionKey.OP_READ, buffer);
    }

    // NIO 的性能瓶颈
//    read>0  需要 注册write ，否则不会触发 witeHandler
//    client.register(key.selector,  SelectiongKey.OP_WRITE, buffer);
//    OP_WRITE 就是关心 send queue 是否有空间
    @SneakyThrows
    private void readHandler(SelectionKey key) {
        SocketChannel clinet = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.clear();
        int read = 0;
        while (true){
            read = clinet.read(buffer);
            if(read > 0){
                buffer.flip();
                while (buffer.hasRemaining()){
                    clinet.write(buffer);
                }
                buffer.clear();
            } else if(read == 0){
                break;
            } else {
                clinet.close();
                break;
            }
        }
    }


//    单线程版本：
//    存在问题：除了每个 key(fd) 需要耗时
//    此时while 会被阻塞后续fd处理
//    redis 的实现类似
    @SneakyThrows
    private void writeHandler(SelectionKey key) {
        SocketChannel clinet = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        while (buffer.hasRemaining()){
            clinet.write(buffer);
        }
    }





}
