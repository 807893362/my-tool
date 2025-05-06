package com.example.demo.lombok;

/**
 * @SneakyThrows
 * - 这个注解用在方法上，可以将方法中的代码用try-catch语句包裹起来，
 * 捕获异常并在catch中用Lombok.sneakyThrow(e)把异常抛出，
 * 可以使用@SneakyThrows(Exception.class)的形式指定抛出哪种异常
 *
 * @Cleanup
 * - 能够自动释放资源; 和 try-with-resources的区别: try-with-resources和lombok的@Cleanup
 *
 * @NotNull
 * - 这个注解可以用在成员方法或者构造方法的参数上，会自动产生一个关于此参数的非空检查，如果参数为空，则抛出一个空指针异常。
 *
 * @Synchronized
 * - 作用于方法，可以替换 synchronized 关键字或 lock 锁
 */
public class MethodLombokTest {
}


