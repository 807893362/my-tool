package com.example.demo.lambda.stream.result;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * 返回stream处理后的元素最小值
 */
public class MinTest {

    public static void main(String[] args) {

        long min = Stream.of(1, 2, 3).min(Comparator.comparing(Integer::intValue)).get();

        System.err.println(min);

    }

}
