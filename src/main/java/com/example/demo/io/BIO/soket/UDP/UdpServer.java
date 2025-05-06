package com.example.demo.io.BIO.soket.UDP;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.net.*;

public class UdpServer {

    @SneakyThrows
    public static void main(String[] args) {
        // 创建DatagramSocket对象，并指定监听的端口号
        @Cleanup DatagramSocket socket = new DatagramSocket(12345);
        System.out.println("等待客户端连接...");

        byte[] receiveData = new byte[1024];

        while (true) {
            // 创建接收数据的DatagramPacket对象
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // 接收客户端发送的数据
            socket.receive(receivePacket);

            // 获取客户端的IP地址和端口号
            InetAddress clientAddress = receivePacket.getAddress();
            int clientPort = receivePacket.getPort();

            // 将接收到的数据转换为字符串
            String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("收到客户端消息：" + message);

            if (message.equalsIgnoreCase("exit")) {
                // 如果接收到终止标志，退出循环
                break;
            }

            // 构造发送数据的字节数组
            String response = "已收到你的消息：" + message;
            byte[] sendData = response.getBytes();

            // 创建发送数据的DatagramPacket对象
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);

            // 发送响应给客户端
            socket.send(sendPacket);
        }

    }

}
