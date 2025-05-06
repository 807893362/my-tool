package com.example.demo.lambda.stream.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 仅保留集合前面指定个数的元素，返回新的stream流
 */
public class LimitTest {

    public static void main(String[] args) {
        List<Integer> collect = Stream.of(1, 2, 3).limit(2).collect(Collectors.toList());
        System.err.println(collect);
    }

}
