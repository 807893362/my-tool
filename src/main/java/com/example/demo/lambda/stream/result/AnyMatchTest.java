package com.example.demo.lambda.stream.result;

import java.util.Arrays;
import java.util.List;

/**
 * 返回一个boolean值，类似于isContains(),用于判断是否有符合条件的元素
 * - 只要有一个满足条件，返回true
 */
public class AnyMatchTest {

    public static void main(String[] args) {

        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        // list中有大于4的int 返回true
        boolean flag = list.stream().anyMatch(x -> x > 4);
        System.out.println(flag);

        // list中有大于3的int 返回true
        flag = list.stream().anyMatch(x -> x > 3);
        System.out.println(flag);

    }


}
