package com.example.demo.io.NIO;

import lombok.SneakyThrows;

import java.net.Socket;
import java.nio.channels.Selector;
import java.util.Map;

/**
 * Reactor模式
 */
public class Server {

    interface ChannelHandler{
        void channelReadable(Channel channel);
        void channelWritable(Channel channel);
    }

    class Channel{
        Socket socket;
        String event;
        private boolean eventX(int select){
            event = String.valueOf(select);
            return true;
        }
    }

    //IO线程主循环:
    class IoThread extends Thread{

        Map<Channel, ChannelHandler> handlerMap;//所有channel的对应事件处理器

        @SneakyThrows
        public void run(){
            Channel channel = new Channel();
            while(channel.eventX(Selector.open().select())){//选择就绪的事件和对应的连接
                if(channel.event=="accept"){
                    registerNewChannelHandler(channel);//如果是新连接，则注册一个新的读写处理器
                }
                if(channel.event=="write"){
                    getChannelHandler(channel).channelWritable(channel);//如果可以写，则执行写事件
                }
                if(channel.event=="read"){
                    getChannelHandler(channel).channelReadable(channel);//如果可以读，则执行读事件
                }
            }
        }

        private ChannelHandler getChannelHandler(Channel channel) {
            return null;
        }

        private void registerNewChannelHandler(Channel channel) {
        }

    }

}
