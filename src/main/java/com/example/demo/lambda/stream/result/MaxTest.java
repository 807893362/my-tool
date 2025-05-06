package com.example.demo.lambda.stream.result;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * 返回stream处理后的元素最大值
 */
public class MaxTest {

    public static void main(String[] args) {

        long max = Stream.of(1, 2, 3).max(Comparator.comparing(Integer::intValue)).get();

        System.err.println(max);

    }

}
