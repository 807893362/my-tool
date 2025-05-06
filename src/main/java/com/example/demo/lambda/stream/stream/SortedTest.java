package com.example.demo.lambda.stream.stream;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 对stream中所有的元素按照指定规则进行排序，返回新的stream流
 */
public class SortedTest {

    public static void main(String[] args) {

        List<Integer> collect = Stream.of(-1, 3, 2, 0, 231, 21).sorted().collect(Collectors.toList());
        System.err.println(collect);

        List<String> collect1 = Stream.of("ca", "ab", "dc", "asd", "sds").sorted(Comparator.comparing(x -> {return x;})).collect(Collectors.toList());
        System.err.println(collect1);

        // 正序
        Comparator<Integer> ageComparator1 = (x1, x2) -> x1 - x2;
        List<Integer> collect2 = Stream.of(-1, 3, 2, 0, 231, 21).sorted(ageComparator1).collect(Collectors.toList());
        // 倒序
        Comparator<Integer> ageComparator2 = (x1, x2) -> x2 - x1;
        List<Integer> collect3 = Stream.of(-1, 3, 2, 0, 231, 21).sorted(ageComparator2).collect(Collectors.toList());
        List<Integer> collect4 = Stream.of(-1, 3, 2, 0, 231, 21).sorted(ageComparator1.reversed()).collect(Collectors.toList());
        System.err.println(collect2);
        System.err.println(collect3);
        System.err.println(collect4);

        // 头换到尾部
        Comparator<Integer> ageComparator3 = (x1, x2) -> {return x2;};
        List<Integer> collect5 = Stream.of(-1, 3, 2, 0, 231, 21).sorted(ageComparator3).collect(Collectors.toList());
        System.err.println(collect5);
    }

}
