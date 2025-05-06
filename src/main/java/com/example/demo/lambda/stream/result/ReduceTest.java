package com.example.demo.lambda.stream.result;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 将一个Stream中的所有元素反复结合起来，得到一个结果
 * reduce是对Stream元素进行聚合求值，最常见的就是讲Stream的一连串的合成为单个值。
 * - reduce方法有三个重载方法
 */
public class ReduceTest {

    public static void main(String[] args) {
        List<Integer> numList = Arrays.asList(1,2,3,4,5);

        // 第一个接受BinaryOperator的lambada表达式
        Optional<Integer> result = numList.stream().reduce((a, b) -> a + b );
        System.err.println(result);
        // 第二个签名实现和第一个唯一的区别是它首次执行的时候，表达式第一个参数并不是Stream的第一个元素，而是通过签名的第一个参数identity.
        Integer result1 = numList.stream().reduce(100, (a,b) -> a + b );
        System.err.println(result1);
        /**
         * - 第三个参数比较复杂，由于前面两种实现有个缺陷，计算结果必须和stream的元素类型相同
         *  ，如上面代码:stream 中类型为int,那么计算结果必须为int。导致灵活性不足。
         *
         * - 这个方法有三个参数，前两个参数和第二个方法的是差不多的，唯一的区别就是对第二个方法的缺点进行了改进
         *  ，为什么说是缺点呢，因为第二个方法限制了初始值和返回值都必须是流元素类型
         *  ，但是这个方法可以然后我们自定义初始值和返回值的类型了，这个就极大的扩展了方法的可用性（有点类似collect的方法）。
         *
         * - 第三个参数combiner，就是用来合并的，也是前面提到的，为什么第三个方法能够使用并行流的重要原因。
         *   因为我们使用并行流的时候会将流元素分成n组再各自的线程中执行，执行完成之后结果在各个线程中如果你没有合并返回的结果是不完整的
         *  ，所以只有有合并参数的第三个方法可以使用并行流。同时反过来说明只有只用了并行流这个参数才有执行的意义。
         */
        String result2 = numList.stream().reduce("__", (a, b) -> a += String.valueOf(b), (x, t) -> null);
        System.err.println(result2);
        // 把上面的的那个案例有单行流改成了并行流，这个结果是不一定的，可能9可能8可能7，看并行流数量
        String result3 = Stream.of(1, 2, 3).parallel().reduce(String.valueOf("1"),
                (acc, i) -> String.valueOf(Integer.valueOf(acc) + i),
                (acc1, acc2) -> String.valueOf(Integer.valueOf(acc1) + Integer.valueOf(acc2)));
        System.err.println(result3);
    }

}
