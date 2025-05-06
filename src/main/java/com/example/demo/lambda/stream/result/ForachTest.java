package com.example.demo.lambda.stream.result;

import java.util.ArrayList;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 无返回值，对元素进行逐个遍历，然后执行给定的处理逻辑
 */
public class ForachTest {

    public static void main(String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("abc");
        list.add("def");
        list.add("ghi");

        Stream<String> streamA = list.stream();

        // 对每一个元素进行打印输出
        streamA.forEach((String s) -> {
            System.out.println(s);
        });

        list.stream().forEach(s -> System.out.println(s));

        list.stream().forEach(System.out::println);

    }

}
