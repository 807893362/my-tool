package com.example.demo.io.Netty;

import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import javax.websocket.ClientEndpoint;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class NettyWSClient {

    @SneakyThrows
    public static void main(String[] args) {
        URI uri = new URI("ws://127.0.0.1:9000/sdsdsdsdddddddddddddddd");
        MyWebSocketClient client = new MyWebSocketClient(uri);
        client.connect();
        while (true){
            TimeUnit.SECONDS.sleep(5);
            client.send("hello world!");
        }
    }


    static class MyWebSocketClient extends WebSocketClient {
        private CountDownLatch closeLatch;
        private String message;

        public MyWebSocketClient(URI serverUri) {
            super(serverUri, new Draft_6455());
            closeLatch = new CountDownLatch(1);
        }

        @Override
        public void onError(Exception e) {
            e.printStackTrace();
        }

        /**
         * 生命连接打开
         */
        @Override
        public void onOpen(ServerHandshake handshakedata) {
            System.out.println("opened connection");
        }

        @Override
        public void onMessage(String message) {
            System.out.println("received: " + message);
            this.message=message;
        }

        /**
         * 监听服务端响应信息
         * message是服务端向客户机返回的文本消息
         */
        @Override
        public void onMessage(ByteBuffer bytes) {
            System.out.println("bytes: " + bytes);
        }

        /**
         * 连接关闭
         */
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
            closeLatch.countDown();
        }

        public String getMessage() {
            return message;
        }
    }


}
