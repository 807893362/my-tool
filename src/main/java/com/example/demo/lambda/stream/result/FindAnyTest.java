package com.example.demo.lambda.stream.result;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 找到任何一个符合条件的元素时则退出流处理，这个对于串行流时与findFirst相同，对于并行流时比较高效，任何分片中找到都会终止后续计算逻辑
 */
public class FindAnyTest {

    public static void main(String[] args) {

        List<String> lst1 = Arrays.asList("Jhonny", "David", "Jack", "Duke", "Jill","Dany","Julia","Jenish","Divya");
        List<String> lst2 = Arrays.asList("Jhonny", "David", "Jack", "Duke", "Jill","Dany","Julia","Jenish","Divya");

        Optional<String> findFirst = lst1.parallelStream().filter(s -> s.startsWith("D")).findFirst();
        Optional<String> fidnAny = lst2.parallelStream().parallel().filter(s -> s.startsWith("D")).findAny();

        System.out.println(findFirst.get()); //总是打印出David
        System.out.println(fidnAny.get()); //会随机地打印出Jack/Jill/Julia

    }


}
