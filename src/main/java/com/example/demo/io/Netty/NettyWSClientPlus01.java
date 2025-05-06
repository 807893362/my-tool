package com.example.demo.io.Netty;

import com.example.demo.io.Netty.utils.MyWSWriter;
import com.example.demo.io.Netty.utils.Packet;
import com.example.demo.io.Netty.utils.PacketCodeC;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.SneakyThrows;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.websocket.ClientEndpoint;
import java.io.FileInputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.security.KeyStore;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@ClientEndpoint
public class NettyWSClientPlus01 {

    // 服务端如果打开，客户端也必须打开
    private static final boolean SSL_BOTHWAY = false;

    @SneakyThrows
    public static void main(String[] args) {

        SSLContext sslContext = getClientContext();

        String token = UUID.randomUUID().toString();
        System.err.println("token: " + token);
        URI uri = new URI("ws://127.0.0.1:9000/" + token);
        MyWebSocketClient client = new MyWebSocketClient(uri);

        client.setSocketFactory(sslContext.getSocketFactory());
        client.connect();
        new MyWSWriter(client, Packet.Command.TEXT, 1);
    }

    static class MyWebSocketClient extends WebSocketClient {
        private CountDownLatch closeLatch;

        public MyWebSocketClient(URI serverUri) {
            super(serverUri, new Draft_6455());
            closeLatch = new CountDownLatch(1);
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
        }

        /**
         * 监听服务端响应信息
         * message是服务端向客户机返回的文本消息
         */
        @Override
        public void onMessage(ByteBuffer bytes) {
            ByteBuf byteBuf = Unpooled.wrappedBuffer(bytes);
            Packet decode = PacketCodeC.INSTANCE.decode(byteBuf);
            if(decode instanceof Packet.TxtPacket){
                System.out.println("收到服务端消息:" + ((Packet.TxtPacket) decode).getTxt());
            }
        }

        /**
         * 连接关闭
         */
        @Override
        public void onClose(int code, String reason, boolean remote) {
            System.out.println("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " Reason: " + reason);
            closeLatch.countDown();
        }

        @Override
        public void onError(Exception e) {
            System.err.println("error " + System.currentTimeMillis() + " =>");
            e.printStackTrace();
            closeLatch.countDown();
        }

    }

    private static final String PROTOCOL = "TLS";
    private static SSLContext sslContext;
    public static final String rsa = "/data/project-pri/personal-master-client/src/main/java/com/example/demo/io/Netty/jkl/";

    @SneakyThrows
    public static SSLContext getClientContext(){
        if(sslContext !=null) return sslContext;
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream(rsa + "tclient.ks"), "clientpublicpass".toCharArray());
        tmf.init(tks);

        if(SSL_BOTHWAY){
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(rsa + "kclient.ks"), "clientpass".toCharArray());
            kmf.init(ks, "clientpass".toCharArray());
        }

        sslContext = SSLContext.getInstance(PROTOCOL);
        sslContext.init(SSL_BOTHWAY?kmf.getKeyManagers():null, tmf.getTrustManagers(), null);
        return sslContext;
    }

}
