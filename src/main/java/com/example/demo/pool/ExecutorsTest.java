package com.example.demo.pool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 运行新任务的简单接口
 * - 在阿里巴巴Java开发手册中明确指出，不允许使用Executors创建线程池。
 */
public class ExecutorsTest {


    public static void main(String[] args) {
        /* ExecutorService*/
        ExecutorService
        // 创建固定数目线程的线程池
        executorService = Executors.newFixedThreadPool(10);
        // 创建一个可缓存的线程池，调用execute 将重用以前构造的线程（如果线程可用）。如果没有可用的线程
        // ，则创建一个新线程并添加到池中。终止并从缓存中移除那些已有 60 秒钟未被使用的线程
        executorService = Executors.newCachedThreadPool();
        // 创建一个单线程化的Executor
        executorService = Executors.newSingleThreadExecutor();
        // 创建一个支持定时及周期性的任务执行的线程池，多数情况下可用来替代Timer类
        executorService = Executors.newScheduledThreadPool(10);


        /* ThreadFactory*/
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 默认线程工厂，创建一个新的、非守护的线程，并且不包含特殊的配置信息。
        executor.setThreadFactory(Executors.defaultThreadFactory());
        // 通过这种方式创建出来的线程，将与创建privilegedThreadFactory的线程拥有相同的访问权限
        // 、 AccessControlContext、ContextClassLoader。如果不使用privilegedThreadFactory
        // ， 线程池创建的线程将从在需要新线程时调用execute或submit的客户程序中继承访问权限。
        executor.setThreadFactory(Executors.privilegedThreadFactory());

        /**
         * submit execute 区别
         * - 两者执行任务最后都会通过Executor的execute方法来执行
         */
        // 但对于submit，会将runnable物件包装成FutureTask<Object>,其run方法会捕捉被包装的Runnable
        // Object的run方法抛出的Throwable异常，待submit方法所返回的的Future Object调用get方法时
        // ，将执行任务时捕获的Throwable Object包装成java.util.concurrent.ExecutionException来抛出。
        Future<?> submit = executor.submit(() -> {});
        // 而对于execute方法，则会直接抛出异常，该异常不能被捕获，想要在出现异常时做些处理
        // ，可以实现Thread.UncaughtExceptionHandler接口：
        executor.execute(() ->{});
    }

}
