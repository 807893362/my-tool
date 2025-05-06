package com.example.demo.lambda;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

/**
 * Consumer: 消费性接口
 * Consumer通过名字可以看出它是一个消费函数式接口，主要针对的是消费（1..n 入参， 无返回）这个场景
 * @FunctionalInterface
 * public interface Consumer<T> {
 *     void accept(T t);
 * }
 */
public class ConsumerTest {

    public static void main(String[] args) {
        Consumer<String> consumer = (t)-> {
            System.err.println(t);
        };
        // check
        List<String> list = Lists.newArrayList("1", "2", "3", "4", "5", "6");
        list.forEach(t -> consumer.accept(t)); //打印数组
    }

}
