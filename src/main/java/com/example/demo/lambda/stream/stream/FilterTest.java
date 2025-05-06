package com.example.demo.lambda.stream.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 按照条件过滤符合要求的元素， 返回新的stream流。
 *
 * 在JDK1.8中，Collection以及其子类新加入了removeIf方法，
 * 作用是按照一定规则过滤集合中的元素。能和filter实现一样的功能，但是要注意用法与filter的差别。
 */
public class FilterTest {



    public static void main(String[] args) {

        // filter
        List<Integer> collect = Stream.of(1, 2, 3).filter(x -> x > 2).collect(Collectors.toList());
        System.err.println(collect);

        // Collection
        Collection<Integer> collection = new ArrayList<>();
        collection.add(1);
        collection.add(2);
        collection.add(3);
        collection.removeIf(
            x -> x <3
        );
        System.err.println(collection);

    }


}
