package com.example.demo.io.BIO.soket.TCP;

import lombok.Cleanup;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TcpServer {

    @SneakyThrows
    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(100);//线程池

        // 创建ServerSocket对象，指定监听的端口号
        ServerSocket serverSocket = new ServerSocket(12345);

        System.out.println("等待客户端连接...");

        int clientNum = 1;
        while (!Thread.currentThread().isInterrupted()){
            // 监听客户端的连接请求
            Socket clientSocket = serverSocket.accept();
            System.out.format("客户端%s已连接 \n", clientNum);
            executor.execute(new SocketThread(clientSocket, clientNum));
            clientNum++;
        }
    }

    static class SocketThread implements Runnable{

        // 句柄
        private Socket clientSocket;

        private int id;

        public SocketThread(Socket clientSocket, int id) {
            this.clientSocket = clientSocket;
            this.id = id;
        }

        @Override
        @SneakyThrows
        public void run() {

            // 获取输入流和输出流 输入流和输出流是通过socket对象来进行数据传输的。
            @Cleanup BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            @Cleanup PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String message;

            while (true) {
                // 读取客户端发送的信息
                message = in.readLine();

                if (message.equalsIgnoreCase("exit")) {
                    // 如果接收到终止标志，退出循环
                    break;
                }

                System.out.format("%s 收到客户端消息：%s \n", id, message);

                // 发送响应给客户端
                out.println("已收到你的消息：" + message + ",id:" + id);
            }

        }
    }

}
