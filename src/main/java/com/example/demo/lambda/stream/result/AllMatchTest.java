package com.example.demo.lambda.stream.result;

import java.util.List;

/**
 * 返回一个boolean值，用于判断是否所有元素都符合条件
 * - 所有元素都满足条件，返回true
 */
public class AllMatchTest {

    public static void main(String[] args) {

        List<Integer> list = List.of(2,5,8,9,4,20,11,43,55);

        boolean bo = list.stream().allMatch(a -> a>1);

        System.out.println(bo);

        boolean bo2 = list.stream().allMatch(a -> a>10);

        System.out.println(bo2);
    }

}
