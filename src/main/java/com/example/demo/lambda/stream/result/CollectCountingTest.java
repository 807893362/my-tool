package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.People;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Stream流中数据总个数
 * -Collectors.counting();
 */
public class CollectCountingTest {

    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(1, "小王", 1));
        peopleList.add(new People(3, "小李", 3));
        peopleList.add(new People(2, "小张", 2));
        peopleList.add(new People(4, "小皇", 4));
        peopleList.add(new People(4, "小皇", 4));

        Long count = peopleList.stream().collect(Collectors.counting());
        System.out.println("数量为:"+count);
    }


}
