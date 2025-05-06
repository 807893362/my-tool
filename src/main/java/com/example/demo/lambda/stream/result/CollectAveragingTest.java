package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.People;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Stream流中数据平均数
 * -Collectors.averagingInt();
 */
public class CollectAveragingTest {

    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(1, "小王", 1));
        peopleList.add(new People(3, "小李", 3));
        peopleList.add(new People(2, "小张", 2));
        peopleList.add(new People(4, "小皇", 4));

        Double avgScore1 = peopleList.stream().collect(Collectors.averagingInt(new ToIntFunction<People>() {
            @Override
            public int applyAsInt(People value) {
                return value.getJgid();
            }
        }));
        //或者使用lambda表达式取jgid平均值
        Double avgScore2 = peopleList.stream().collect(Collectors.averagingInt(People::getJgid));

        System.out.println("分数平均值:"+avgScore1);
        System.out.println("分数平均值:"+avgScore2);
    }


}
