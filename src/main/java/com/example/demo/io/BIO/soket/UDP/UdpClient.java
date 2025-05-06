package com.example.demo.io.BIO.soket.UDP;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpClient {

    @SneakyThrows
    public static void main(String[] args) {
        // 创建DatagramSocket对象
        @Cleanup DatagramSocket socket = new DatagramSocket();

        InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
        int serverPort = 12345;

        // 从控制台读取用户输入
        @Cleanup BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String message;

        while (true) {
            System.out.println("请输入要发送的信息（输入 'exit' 退出）：");
            message = reader.readLine();

            if (message.equalsIgnoreCase("exit")) {
                // 如果用户输入 'exit'，退出循环
                break;
            }

            byte[] sendData = message.getBytes();

            // 创建发送数据的DatagramPacket对象
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);

            // 发送数据
            socket.send(sendPacket);

            byte[] receiveData = new byte[1024];

            // 创建接收数据的DatagramPacket对象
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            // 接收服务端的响应
            socket.receive(receivePacket);

            // 将接收到的数据转换为字符串并打印
            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("服务端响应：" + response);
        }

    }

}
