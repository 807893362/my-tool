package com.example.demo.lambda.stream.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 将流转换为数组
 * - 收集 Stream 流中的数据到数组中
 */
public class ToArrayTest {

    public static void main(String[] args) {
        List<String> peopleList = new ArrayList<String>();
        peopleList.add("小王");
        peopleList.add("小李");

        //1.使用无参,收集到数组,返回值为 Object[](Object类型将不好操作)
        Object[] objects = peopleList.stream().toArray();
        for (Object o : objects) {//此处无法使用.length() 等方法
            System.out.println("data:" + o);
        }

        //2.使用有参,可以指定将数据收集到指定类型数组,方便后续对数组的操作
        String[] people = peopleList.stream().toArray(String[]::new);
        for (String str : people) {
            System.out.println(str);
        }
    }


}
