package com.example.demo.lambda.stream.stream;

import java.util.stream.Stream;

/**
 * JDK9新增，传入一个断言参数当第一次断言为false时停止，返回前面断言为true的元素
 * - filter 与 takeWhile 的比较
 *  - filter将从流中移除不满足条件的所有项目。
 *  - takeWhile将在第一次出现不满足条件的项时中止流。
 */
public class TakeWhileTest {


    public static void main(String[] args) {
        Stream.of(1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1)
                .filter(x -> x < 4 )
                .forEach(System.err::print);
        System.err.println("\n------");
        Stream.of(1,2,3,4,5,6,7,8,9,10,9,8,7,6,5,4,3,2,1)
                .takeWhile(x -> x < 4 )
                .forEach(System.out::print);
    }


}
