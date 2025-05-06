package com.example.demo.lambda.stream.stream;

/**
 * 对stream流中的每个元素进行逐个遍历处理，返回处理后的stream流
 * - 存在此方法主要是为了支持调试，您需要在其中查看元素流过管道中特定点的情况。
 * - 从Java 9开始，如果元素的数量预先已知并且在流中保持不变，则由于性能优化，
 *   将不会执行.peek()语句。可以通过命令(正式)更改元素数量(例如， .filter(x-> true)。
 *
 */
public class PeekTest {


    public static void main(String[] args) {

    }


}
