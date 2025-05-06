package com.example.demo.lambda;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @FunctionalInterface注解
 * 有且只有一个抽象方法的接口被称为函数式接口，函数式接口适用于函数式编程的场景，
 * Lambda就是Java中函数式编程的体现，可以使用Lambda表达式创建一个函数式接口的对象，
 * 一定要确保接口中有且只有一个抽象方法，这样Lambda才能顺利的进行推导。
 */
public class FunctionalInterfaceTest<T> {

    @FunctionalInterface
    public interface Consume<T>{
        boolean test(T t);
    }

    public static void main(String[] args) {
        // 传统写法
        Consume<String> o = new Consume<String>() {
            @Override
            public boolean test(String s) {
                return false;
            }
        };
        // lambda
        Consume<String> n = (s -> {return false;});

        // check
        List<String> list = Lists.newArrayList("1", "2", "3", "4", "5", "6");
        list.forEach(t -> System.err.format("%s-%s \n",t, n.test(t))); //打印数组
    }


}


