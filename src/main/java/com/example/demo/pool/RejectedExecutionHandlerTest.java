package com.example.demo.pool;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池拒绝策略
 * - AbortPolicy：直接抛出异常，这是默认策略
 * - CallerRunsPolicy：用调用者所在的线程来执行任务
 * - DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务
 * - DiscardPolicy：直接丢弃任务
 */
public class RejectedExecutionHandlerTest {

    public static void main(String[] args) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 直接抛出异常，这是默认策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 用调用者所在的线程来执行任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 丢弃阻塞队列中靠最前的任务，并执行当前任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
        // 直接丢弃任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
    }

}
