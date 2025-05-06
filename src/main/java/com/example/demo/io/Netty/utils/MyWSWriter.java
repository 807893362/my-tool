package com.example.demo.io.Netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import lombok.Cleanup;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * 去深刻理解泛型 以及类加载器
 * - java 判断泛型是不是某个类 :
 */
public class MyWSWriter extends Thread{

    private WebSocketClient channel;
    private Packet.Command command;

    /*秒*/
    private int readerIdleTime;

    public MyWSWriter(WebSocketClient channel, Packet.Command command, int readerIdleTime) {
        this.channel = channel;
        this.command = command;
        this.readerIdleTime = readerIdleTime;
        // 发起心跳
        new Thread(()->{
            while (true){
                try {
                    TimeUnit.SECONDS.sleep(readerIdleTime);
                } catch (InterruptedException e) {
                }
                ByteBuf ping = PacketCodeC.INSTANCE.encode(ByteBufAllocator.DEFAULT, new Packet.HeartPacket());
                channel.send(ping.nioBuffer());
            }
        }).start();
        this.start();
    }

    @SneakyThrows
    @Override
    public void run() {
        // 从控制台读取用户输入
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message = null;
        while (true) {
            TimeUnit.SECONDS.sleep(1);
            System.err.println("请输入要发送的信息（输入 'exit' 退出）：" + message);
            message = reader.readLine();
            if (message.equalsIgnoreCase("exit")) {
                // 如果用户输入 'exit'，发送终止标志给服务端并退出循环
                System.err.println("exit");
                break;
            }

            Packet packet;
            if(command.packet == Packet.TxtPacket.class){
                packet = new Packet.TxtPacket(message);
            } else {
                System.err.println("请对MyWriter扩展，暂只支持文本packet");
                return;
            }
            ByteBuf txt = PacketCodeC.INSTANCE.encode(ByteBufAllocator.DEFAULT, packet);
            channel.send(txt.nioBuffer());
        }
    }

}
