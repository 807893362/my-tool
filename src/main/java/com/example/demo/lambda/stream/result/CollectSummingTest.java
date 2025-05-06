package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.People;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Stream流中数据总和
 * -Collectors.summingInt();
 */
public class CollectSummingTest {

    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(1, "小王", 1));
        peopleList.add(new People(3, "小李", 3));
        peopleList.add(new People(2, "小张", 2));
        peopleList.add(new People(4, "小皇", 4));

        //取jgid总和
        Integer sumCollect1 = peopleList.stream().collect(Collectors.summingInt(new ToIntFunction<People>() {
            @Override
            public int applyAsInt(People value) {
                return value.getJgid();
            }
        }));
        //或者使用lambda表达式取jgid总和
        Integer sumCollect2 = peopleList.stream().collect(Collectors.summingInt(People::getJgid));

        System.out.println("jgid总和:" + sumCollect1);
        System.out.println("jgid总和:" + sumCollect2);
    }


}
