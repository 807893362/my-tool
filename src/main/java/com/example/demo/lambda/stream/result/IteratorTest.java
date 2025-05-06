package com.example.demo.lambda.stream.result;

import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * 将流转换为Iterator对象
 */
public class IteratorTest {

    public static void main(String[] args) {
        IntStream intStream = IntStream.of(15, 40, 55, 70, 95, 120);
        PrimitiveIterator.OfInt primIterator = intStream.iterator();
        while (primIterator.hasNext()) {
            System.out.println(primIterator.nextInt());
        }

    }

}
