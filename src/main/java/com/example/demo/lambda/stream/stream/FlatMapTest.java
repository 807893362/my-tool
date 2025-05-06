package com.example.demo.lambda.stream.stream;

import java.util.Arrays;
import java.util.List;

/**
 * 将已有元素转换为另一个对象类型，一对多逻辑，即原来一个元素对象可能会转换为1个或者多个新类型的元素，返回新的stream流
 * - map()和flatMap()方法之间的区别。
 * - 虽然看起来这两种方法都做同样的事情，都是做的映射操作，但实际上差之毫厘谬以千里。
 */
public class FlatMapTest {

    public static void main(String[] args) {

        // map
        List<String> fun1 = Arrays.asList("one", "two", "three");
        List<String> fun2 = Arrays.asList("four", "five", "six");
        List<List<String>> nestedList = Arrays.asList(fun1, fun2);
        nestedList.stream().map(x -> {
            return x.stream().map(a -> a.toUpperCase());
        }).forEach(x -> System.err.println(x));

        // flatMap
        // 相当于在.flatMap(x -> x.stream())这个时候我们把x.stream()返回的stream对象合并成了一个新的stream对象。这一点在Stream类的方法注释中找到了印证。
        List<List<String>> nestedListFlat = Arrays.asList(fun1, fun2);
        nestedListFlat.stream().map(x -> {
            return x.stream().map(a -> a.toUpperCase());
        }).forEach(x ->x.forEach(a-> System.err.println(a)));

    }

}
