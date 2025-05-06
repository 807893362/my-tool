package com.example.demo.lambda.stream.stream;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将两个流的数据合并起来为1个新的流，返回新的stream流
 */
public class ConcatTest {


    public static void main(String[] args) {

        List<Integer> collect1 = Stream.of(1, 2, 3).collect(Collectors.toList());
        List<Integer> collect2 = Stream.of(4, 5, 6).collect(Collectors.toList());

        List<Integer> ts = Stream.concat(collect1.stream(), collect2.stream()).toList();
        System.err.println(ts);

    }

}
