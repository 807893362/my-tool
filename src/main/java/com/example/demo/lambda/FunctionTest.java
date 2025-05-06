package com.example.demo.lambda;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Function<T,R>: 函数型接口
 * Function 接口的名字不太能轻易看出来它的场景，它主要针对的则是 转换（有入参，有返回，其中T是入参，R是返回）这个场景，
 * 其实说转换可能也不太正确，它是一个覆盖范围比较广的场景，你也可以理解为扩展版的Consumer
 * @FunctionalInterface
 * public interface Function<T, R> {
 *     R apply(T t);
 * }
 */
public class FunctionTest {

    public static void main(String[] args) {

        Function<String, Integer> o = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return null;
            }
        };

        Function<String, Integer> n = (s) ->{
            return 0;
        };

        // 场景：map将list中所有的元素的类型由 String 通过 Integer.parseInt的方式转换为Intger。 简单来说就是A => B;
        List<String> list = Lists.newArrayList("1", "2", "3", "4", "5", "6");
        List<Integer> newList = list.stream().map(Integer::parseInt).collect(Collectors.toList());

    }

}

