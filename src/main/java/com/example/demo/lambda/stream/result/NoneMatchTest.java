package com.example.demo.lambda.stream.result;

import java.util.List;

/**
 * 返回一个boolean值， 用于判断是否所有元素都不符合条件
 * - 所有元素都不满足条件，返回true
 */
public class NoneMatchTest {

    public static void main(String[] args) {

        List<String> list = List.of("Mr.zhangsan","Mr.lisi","Mr.wanger","Mr.mazi");

        boolean bl = list.stream().noneMatch(a -> a.indexOf("lisi")>-1);

        System.out.println(bl);

        boolean bl2 = list.stream().noneMatch(a -> a.indexOf("lisi001")>-1);

        System.out.println(bl2);

    }

}
