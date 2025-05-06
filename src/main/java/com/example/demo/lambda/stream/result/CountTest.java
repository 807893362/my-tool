package com.example.demo.lambda.stream.result;

import java.util.stream.Stream;

/**
 * 返回stream处理后最终的元素个数
 */
public class CountTest {

    public static void main(String[] args) {

        long count = Stream.of(1, 2, 3).count();
        System.err.println(count);

    }

}
