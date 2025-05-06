package com.example.demo.lambda;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Supplier: 供给型接口
 * Supplier通过名字比较难看出来它是一个场景的函数式接口，它主要针对的是说获取（无入参，有返回）这个场景，它的代码定义如下：
 * @FunctionalInterface
 * public interface Supplier<T> {
 *     T get();
 * }
 */
public class SupplierTest {

    public static void main(String[] args) {

        Supplier<List<Integer>> o = new Supplier<List<Integer>>() {
            @Override
            public List<Integer> get() {
                return null;
            }
        };

        Supplier<List<Integer>> n = ()-> {
            ArrayList<Integer> list = new ArrayList<>();
            return list;
        };

        /**
         * 场景
         * 将大于等于2的数重新收集成一个集合，其中Collectors.toList()的函数原型为
         * new CollectorImpl<>((Supplier<List<T>>) ArrayList::new, List::add,(left, right) -> { left.addAll(right); return left; },CH_ID)
         * 原型中的ArrayList::new即为Supplier类型
         */
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> newList = list.stream().filter(x -> x >= 2).collect(Collectors.toList());

    }

}
