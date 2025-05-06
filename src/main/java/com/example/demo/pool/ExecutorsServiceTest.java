package com.example.demo.pool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * ExecutorService是Java中对线程池定义的一个接口，它java.util.concurrent包中，在这个接口中定义了和后台任务执行相关的方法：
 * - 可以通过 Executors 创建
 * - execute(Runnable)
 * - submit(Runnable)
 * - submit(Callable)
 * - invokeAny(...)
 * - invokeAll(...)
 */
public class ExecutorsServiceTest {


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // execute、shutdown
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        executorService.execute(new Runnable() {
            public void run() {
                System.out.println("Asynchronous task");
            }
        });
        executorService.shutdown();

        // Future
        Future future = executorService.submit(new Runnable() {
            public void run() {
                System.out.println("Asynchronous task");
            }
        });
        future.get();
    }

}
