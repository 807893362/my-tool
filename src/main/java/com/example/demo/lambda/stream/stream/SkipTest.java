package com.example.demo.lambda.stream.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 跳过集合前面指定个数的元素，返回新的stream流
 * - 跳过前N个元素
 * - 0 不会跳过
 */
public class SkipTest {


    public static void main(String[] args) {

        List<Integer> collect = Stream.of(1, 2, 3).skip(2).collect(Collectors.toList());
        System.err.println(collect);

    }


}


