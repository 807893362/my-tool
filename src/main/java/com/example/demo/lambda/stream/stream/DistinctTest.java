package com.example.demo.lambda.stream.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对Stream中所有元素进行去重，返回新的stream流
 */
public class DistinctTest {

    public static void main(String[] args) {

        List<Integer> collect = Stream.of(1, 2, 3, 4, 1, 2, 0).distinct().collect(Collectors.toList());
        System.err.println(collect);
    }

}
