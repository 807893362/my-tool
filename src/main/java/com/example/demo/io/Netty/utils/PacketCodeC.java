package com.example.demo.io.Netty.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * 编解码
 */
public enum PacketCodeC {

    INSTANCE;


    @Component
    public static class EnumClassInner {
        @PostConstruct
        public void postConstruct() {
        }
    }

    private static final int MAGIC_NUMBER = 0x12345678;

    public ByteBuf encode(ByteBufAllocator alloc, Packet packet) {
        return encode(alloc, packet, Packet.Serializer.DEFAULT);
    }

    public ByteBuf encode(ByteBufAllocator alloc, Packet packet, Packet.Serializer serializer) {

        // 1. 创建 ByteBuf 对象
//        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.ioBuffer();
        ByteBuf byteBuf = alloc.ioBuffer();
        // 2. 序列化 Java 对象
        byte[] bytes = serializer.serialize(packet);
        // 3. 实际编码过程
        // 第一个字段是魔数，通常情况下为固定的几个字节(这边规定为4个字节), 用来表示类型
        byteBuf.writeInt(MAGIC_NUMBER);
        // 版本号，预留字段
        byteBuf.writeByte(packet.getVersion());
        // 序列化算法
        byteBuf.writeByte(serializer.getSerializerAlgorithm().serializeAlgorithm);
        // 指令 最高支持256种指令
        byteBuf.writeByte(packet.getCommand());
        // 数据部分的长度，占四个字节
        byteBuf.writeInt(bytes.length);
        // 数据内容
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Packet decode(ByteBuf byteBuf) {
        // 跳过 magic number
        byteBuf.skipBytes(4);

        // 跳过版本号
        byteBuf.skipBytes(1);

        // 序列化算法标识
        byte serializeAlgorithm = byteBuf.readByte();

        // 指令
        byte command = byteBuf.readByte();

        // 数据包长度
        int length = byteBuf.readInt();

        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);

        Class<? extends Packet> requestType = Packet.Command.invoke(command);
        Packet.Serializer serializer = Packet.SerializeAlgorithm.invoke(serializeAlgorithm);

        if (requestType != null && serializer != null) {
            return serializer.deserialize(requestType, bytes);
        }

        return null;
    }

    public static class Decoder extends MessageToMessageDecoder<ByteBuf> {

        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
            out.add(PacketCodeC.INSTANCE.decode(byteBuf));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    public static class Encoder extends MessageToMessageEncoder<Packet> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
            out.add(PacketCodeC.INSTANCE.encode(ctx.alloc(), packet));
        }
    }

    public static class WSDecoder extends MessageToMessageDecoder<BinaryWebSocketFrame> {

        @Override
        protected void decode(ChannelHandlerContext ctx, BinaryWebSocketFrame frame, List<Object> out) throws Exception {
            out.add(PacketCodeC.INSTANCE.decode(frame.content()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    public static class WSEncoder extends MessageToMessageEncoder<Packet> {

        @Override
        protected void encode(ChannelHandlerContext ctx, Packet packet, List<Object> out) throws Exception {
            ByteBuf byteBuf = PacketCodeC.INSTANCE.encode(ctx.alloc(), packet);
            out.add(new BinaryWebSocketFrame(byteBuf));
        }
    }

}
