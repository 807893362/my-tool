package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.People;
import com.example.demo.lambda.stream.model.Person;

import java.util.*;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Stream流中数据聚合
 * -Collectors.maxBy();
 */
public class CollectMaxByTest {

    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(1, "小王", 1));
        peopleList.add(new People(3, "小李", 3));
        peopleList.add(new People(2, "小张", 2));
        peopleList.add(new People(4, "小皇", 4));

        //取最大值
        Optional<People> maxCollect1 = peopleList.stream().collect(Collectors.maxBy(
                new Comparator<People>() {
                    @Override
                    public int compare(People o1, People o2) {
                        //比大小前值比后值大则返回非负数
                        if (o1.getJgid() > o2.getJgid()) {
                            return 0;
                        } else {
                            return -1;
                        }
//                        return o1.getJgid() - o2.getJgid();
                    }
                }
        ));
        //或者使用lambda表达式取最大值
        Optional<People> maxCollect2 = peopleList.stream().collect(Collectors.maxBy((s1, s2) -> s1.getJgid() - s2.getJgid()));
        //或者使用lambda表达式取最小值
        Optional<People> minCollect = peopleList.stream().collect(Collectors.maxBy((s1, s2) -> s2.getJgid() - s1.getJgid()));

        System.out.println("最大值:" + maxCollect1.get());
        System.out.println("最大值:" + maxCollect2.get());
        System.out.println("最小值:" + minCollect.get());
    }


}
