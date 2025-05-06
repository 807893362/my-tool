package com.example.demo.lambda;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Predicate: 断言型接口
 * Predicate主要针对的是判断（有入参，有返回，凡是返回的类型固定为Boolean。可以说Function 是包含Predicate的 ）这个场景，它的代码定义如下：
 * @FunctionalInterface
 * public interface Predicate<T> {
 *     boolean test(T t);
 * }
 */
public class PredicateTest {

    public static void main(String[] args) {

        Predicate<String> o = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        };

        Predicate<String> n1 = (s) ->{ return false;};
        Predicate<String> n2 = (s) -> false;

        // 场景 ：  将大于等于2的数重新收集成一个集合，filter中的 x -> x >= 2就是Predicate接口
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 6);
        List<Integer> newList = list.stream().filter(x -> x >= 2).collect(Collectors.toList());


    }


}
