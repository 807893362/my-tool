package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.People;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

/**
 * Stream流中数据聚合
 * -Collectors.minBy();
 */
public class CollectMinByTest {

    public static void main(String[] args) {
        List<People> peopleList = new ArrayList<>();
        peopleList.add(new People(1, "小王", 1));
        peopleList.add(new People(3, "小李", 3));
        peopleList.add(new People(2, "小张", 2));
        peopleList.add(new People(4, "小皇", 4));

        //取最大值
        Optional<People> maxCollect1 = peopleList.stream().collect(Collectors.minBy(
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
        Optional<People> maxCollect2 = peopleList.stream().collect(Collectors.minBy((s1, s2) -> s1.getJgid() - s2.getJgid()));
        //或者使用lambda表达式取最小值
        Optional<People> minCollect = peopleList.stream().collect(Collectors.minBy((s1, s2) -> s2.getJgid() - s1.getJgid()));
        System.out.println("最小值:" + maxCollect1.get());
        System.out.println("最小值:" + maxCollect2.get());
        System.out.println("最大值:" + minCollect.get());
    }


}
