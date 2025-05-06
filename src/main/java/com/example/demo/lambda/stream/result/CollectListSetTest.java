package com.example.demo.lambda.stream.result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将流转换为指定的类型，通过Collectors进行指定
 * - 1.收集数据到list集合中
 * stream.collect(Collectors.toList())
 * - 2.收集数据到set集合中
 * stream.collect(Collectors.toSet())
 * - 3.收集数据到指定的集合中
 * stream.collect(Collectors.toCollection(Supplier<C> collectionFactory))
 */
public class CollectListSetTest {

    public static void main(String[] args) {
        //Stream 流
        Stream<String> stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //收集流中的数据到集合中
        //1.收集流中的数据到 list
        List<String> list = stream.collect(Collectors.toList());
        System.out.println(list);

        //Stream 流
        stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //2.收集流中的数据到 set
        Set<String> collect = stream.collect(Collectors.toSet());
        System.out.println(collect);

        //Stream 流
        stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //3.收集流中的数据(ArrayList)(不收集到list,set等集合中,而是)收集到指定的集合中
        ArrayList<String> arrayList = stream.collect(Collectors.toCollection(ArrayList::new));
        System.out.println(arrayList);

        //Stream 流
        stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //4.收集流中的数据到 HashSet
        HashSet<String> hashSet = stream.collect(Collectors.toCollection(HashSet::new));
        System.out.println(hashSet);

        //Stream 流
        stream = Stream.of("aaa", "bbb", "ccc", "bbb");
        //distinct()不重复
        List<String> distinctCollect = stream.distinct().collect(Collectors.toList());
        System.out.println(distinctCollect);
    }

}
