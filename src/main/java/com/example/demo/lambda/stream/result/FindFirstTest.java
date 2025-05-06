package com.example.demo.lambda.stream.result;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 找到第一个符合条件的元素时则终止流处理
 * - 大多数情况下，数据量不大的情况下，findAny()也会返回第一个元素，此时效果与findFirst()一致
 */
public class FindFirstTest {

    public static void main(String[] args) {

        List<String> lst1 = Arrays.asList("Jhonny", "David", "Jack", "Duke", "Jill","Dany","Julia","Jenish","Divya");
        List<String> lst2 = Arrays.asList("Jhonny", "David", "Jack", "Duke", "Jill","Dany","Julia","Jenish","Divya");

        Optional<String> findFirst = lst1.parallelStream().filter(s -> s.startsWith("D")).findFirst();
        Optional<String> fidnAny = lst2.parallelStream().parallel().filter(s -> s.startsWith("D")).findAny();

        System.out.println(findFirst.get()); //总是打印出David
        System.out.println(fidnAny.get()); //会随机地打印出Jack/Jill/Julia

    }


}
