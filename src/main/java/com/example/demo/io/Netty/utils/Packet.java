package com.example.demo.io.Netty.utils;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;

/**
 * 传输类 & 序列化工具 & 指令集 & 序列化集
 * @author wws
 */
@Data
public abstract class Packet {
    /**
     * 协议版本
     */
    private Byte version = 1;

    /**
     * 指令
     */
    public abstract Byte getCommand();

    public enum Command {
        HEART(Byte.valueOf("0"), HeartPacket.class),
        LOGIN(Byte.valueOf("1"), LoginRequestPacket.class),
        TEXT(Byte.valueOf("2"), TxtPacket.class),
        ;

        public byte command;
        public Class<? extends Packet> packet;

        Command(Byte command, Class<? extends Packet> packet) {
            this.command = command;
            this.packet = packet;
        }

        public static Class<? extends Packet> invoke(Byte command){
            return Arrays.stream(Command.values()).filter(x -> x.command == command).findFirst().get().packet;
        }
    }

    public enum SerializeAlgorithm {

        FASTJSON(Byte.valueOf("0"), new JSONSerializer()),

        ;

        public byte serializeAlgorithm;
        public Serializer serializer;

        SerializeAlgorithm(Byte serializeAlgorithm, Serializer serializer) {
            this.serializeAlgorithm = serializeAlgorithm;
            this.serializer = serializer;
        }

        public static Serializer invoke(Byte serializeAlgorithm){
            return Arrays.stream(SerializeAlgorithm.values()).filter(x -> x.serializeAlgorithm == serializeAlgorithm).findFirst().get().serializer;
        }
    }

    public interface Serializer {

        Serializer DEFAULT = new JSONSerializer();

        /**
         * 序列化算法
         */
        SerializeAlgorithm getSerializerAlgorithm();

        /**
         * java 对象转换成二进制
         */
        byte[] serialize(Object object);

        /**
         * 二进制转换成 java 对象
         */
        <T> T deserialize(Class<T> clazz, byte[] bytes);
    }

    static class JSONSerializer implements Serializer {

        @Override
        public SerializeAlgorithm getSerializerAlgorithm() {
            return SerializeAlgorithm.FASTJSON;
        }

        @Override
        public byte[] serialize(Object object) {
            return JSON.toJSONBytes(object);
        }

        @Override
        public <T> T deserialize(Class<T> clazz, byte[] bytes) {
            return JSON.parseObject(bytes, clazz);
        }
    }

    @Data
    public static class LoginRequestPacket extends Packet {

        private String token;

        @Override
        public Byte getCommand() {
            return Command.LOGIN.command;
        }
    }

    @Data
    public static class HeartPacket extends Packet {
        @Override
        public Byte getCommand() {
            return Command.HEART.command;
        }
    }

    @Data
    @AllArgsConstructor
    public static class TxtPacket extends Packet {
        private String txt;
        @Override
        public Byte getCommand() {
            return Command.TEXT.command;
        }
    }

}
