package com.example.demo.lambda.stream.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将已有元素转换为另一个对象类型，一对一逻辑，返回新的stream流
 */
public class MapTest {

    public static void main(String[] args) {

        List<Integer> collect = Stream.of(1, 2, 3).map((x) -> x + 3).collect(Collectors.toList());

        System.err.println(collect);

    }

}
