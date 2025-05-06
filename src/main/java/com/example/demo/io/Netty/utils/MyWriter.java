package com.example.demo.io.Netty.utils;

import io.netty.channel.Channel;
import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * 去深刻理解泛型 以及类加载器
 * - java 判断泛型是不是某个类 :
 */
public class MyWriter extends Thread{

    private Channel channel;
    private Packet.Command command;

    public MyWriter(Channel channel, Packet.Command command) {
        this.channel = channel;
        this.command = command;
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
            channel.writeAndFlush(packet);
        }
    }

}
