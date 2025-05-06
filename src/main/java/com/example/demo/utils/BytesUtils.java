
package com.example.demo.utils;

public class BytesUtils {

    /**
     * 拼接byte数组
     * @param data1
     * @param data2
     * @return 拼接后数组
     */
    public static byte[] addBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    /**
     * 数组截取
     * @param bytes 原数据
     * @param s 开始
     * @param e 结束
     * @return
     */
    public static byte[] subBytes(byte[] bytes, int s, int e) {
        byte[] b1 = null;
        int l = e - s;
        if(null != bytes && l > 0){
            b1 = new byte[l];
            //从原始数组s位置开始截取后面所有
            System.arraycopy(bytes, s, b1, 0, l);
        }
        return b1;
    }

    public static byte[] subBytes(byte[] bytes, int s) {
        byte[] b1 = null;
        int l = bytes.length - s;
        if(null != bytes && l > 0){
            b1 = new byte[l];
            //从原始数组s位置开始截取后面所有
            System.arraycopy(bytes, s, b1, 0, l);
        }
        return b1;
    }

    /**
     * 整数转数组
     * @param res
     * @return
     */
    public static byte[] int2byte(int res) {
        byte[] targets = new byte[4];
        targets[0] = (byte) (res & 0xff);// 最低位
        targets[1] = (byte) ((res >> 8) & 0xff);// 次低位
        targets[2] = (byte) ((res >> 16) & 0xff);// 次高位
        targets[3] = (byte) (res >>> 24);// 最高位,无符号右移。
        return targets;
    }

    /**
     * 数组转整数
     * @param res
     * @return
     */
    public static int byte2int(byte[] res) {
        int targets = 0;
        if(null != res){
            // 一个byte数据左移24位变成0x??000000，再右移8位变成0x00??0000
            targets = (res[0] & 0xff) | ((res[1] << 8) & 0xff00)
                    | ((res[2] << 24) >>> 8) | (res[3] << 24);
        }
        return targets;
    }

}
