package com.example.demo.lambda.stream.result;

import com.example.demo.lambda.stream.model.Person;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 将流转换为指定的类型，通过Collectors进行指定
 * - MAP: toMap()的第一个参数就是用来生成key值的，第二个参数就是用来生成value值的。
 * - 第三个参数用在key值冲突的情况下：如果新元素产生的key在Map中已经出现过了，第三个参数就会定义解决的办法。
 * - 如果key冲突，不加第三个参数会报如下错误: java.lang.IllegalStateException: Duplicate key *
 */
public class CollectMapTest {

    public static void main(String[] args) {
        List<Person> list = new ArrayList();
        list.add(new Person(1, "1"));
        list.add(new Person(2, "2"));
        list.add(new Person(3, "3"));
        Map<Integer, Person> collect = list.stream().collect(Collectors.toMap(Person::getId, Function.identity()));

        list.add(new Person(3, "4"));
        // 第三个参数：(a,b)->a中，如果a与b的key值相同，选择a作为那个key所对应的value值。
        Map<Integer, Person> collect1 = list.stream().collect(Collectors.toMap(Person::getId, Function.identity(), (a,b)->a));
        Map<Integer, Person> collect2 = list.stream().collect(Collectors.toMap(Person::getId, v -> v, (a,b)->b));
        Collection<Person> values = list.stream().collect(Collectors.toMap(Person::getId, Function.identity(), (a, b) -> a)).values();
        long count = list.stream().collect(Collectors.toMap(Person::getId, Function.identity(), (a, b) -> a)).values().stream().count();
        System.out.println(collect);
        System.out.println(collect1);
        System.out.println(collect2);
        System.out.println(values);
        System.out.println(count);

    }


}
