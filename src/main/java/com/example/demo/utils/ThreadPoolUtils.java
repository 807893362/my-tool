
package com.example.demo.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 线程池统一工具类
 * @author dc
 * @date 2019年5月31日
 * @time 上午10:44:27
 */
@Slf4j
public class ThreadPoolUtils {

    /**
     * 初始化时间:3分钟
     */
    private static final long INIT_TIME = 5 * 1000;

    /**
     * 时间单位：ms
     */
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 线程池大小
     */
    private static final int POOL_SIZE;

    static {
        POOL_SIZE = 50;
    }

    /**
     * 固定频率定时执行任务
     * @param runnable 执行的任务
     * @param time     定时执行的时间间隔，单位：ms
     */
    public static ScheduledExecutorService scheduleAtFixedRate(Runnable runnable, long time) {
        RejectedExecutionHandler myRejectedExecutionHandler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                //打印丢弃的任务
                log.error(r.toString() + " is discard");
            }
        };
        ScheduledExecutorService scheduledExecutorService = new ScheduledThreadPoolExecutor(POOL_SIZE, new ThreadFactoryBuilder().build(), myRejectedExecutionHandler);
        scheduledExecutorService.scheduleAtFixedRate(new Task(runnable), INIT_TIME, time, TIME_UNIT);
//        scheduledExecutorService.scheduleWithFixedDelay(new Task(runnable), INIT_TIME, time, TIME_UNIT);
        return scheduledExecutorService;
    }

    private static class Task implements Runnable {
        private Runnable task;
        private String className;
        /**
         * className, lastTime
         */
        private static Map<String, Long> allClass;
        //5分钟
        private static AtomicLong count = new AtomicLong();

        static {
            allClass = new ConcurrentHashMap<>();
        }

        public Task(Runnable task) {
            this.task = task;
            className = this.task.getClass().getSimpleName();

        }

        @Override
        public void run() {
            try {
                long time = System.currentTimeMillis();
                allClass.put(className, time);
                if (count.incrementAndGet() % 1000 == 0) {
                    log.info("all schedul task ={}", allClass);
                }
            } catch (Exception e) {
                log.warn("", e);
            }
            try {
                task.run();
            } catch (Exception e) {
                log.error("ThreadPoolUtils ... 线程:{}", Thread.currentThread().getName(), e);
            }
        }
    }
}
