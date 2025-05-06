package com.example.demo.lambda;

import com.example.demo.lambda.stream.model.Student;

import java.util.*;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

/**
 * 排序
 **/
public class ComparatorTest {


    public static void main(String[] args) {
        // 对包装类型的数组进行排序
        Integer[] a = {1, 2, 3, 0};
        // 直接对 a 排序
        Arrays.sort(a, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        // 不对 a 排序，而是将 a 排序的结果给 a2
        Integer[] a2 = Arrays.stream(a).sorted(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        }).toArray(new IntFunction<Integer[]>() {
            @Override
            public Integer[] apply(int value) {
                return new Integer[value];
            }
        });

        // 对基本数据类型的数组进行排序 —— 不允许自定义排序规则
        int[] b = {1, 3, 2, 4};
        Arrays.sort(b);
        int[] b1 = Arrays.stream(b).sorted().toArray();


        // 多重排序
        List<Student> list = new ArrayList<>();
        // 对 list 按照 先年龄 后 成绩的方式排序
        Collections.sort(list, Comparator.comparing(Student :: getAge).thenComparing(Student :: getScore));
        // 将 list 按照先成绩 再年龄 的方式排序，并将结果赋值给 list2
        List<Student> list2 = list.stream().sorted(Comparator.comparing(Student :: getScore).thenComparing(Student :: getAge)).collect(Collectors.toList());
        System.err.println(list2);
    }



}
