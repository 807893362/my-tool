package com.example.demo.pool.task.spring.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 两种方式：
 * implements AsyncConfigurer
 * extends AsyncConfigurerSupport
 * 几乎没有区别
 */
@Slf4j
@Configuration
public class TaskConfig {

    /**
     * 每次并发执行的线程数(queueCapacity 未满的情况下)
     */
    public static final int corePoolSize = 1;
    /**
     * 每次并发执行的线程数(queueCapacity 已满的情况下)
     */
    public static final int maxPoolSize = 2;
    /**
     * 最大线程数为 maxPoolSize + queueCapacity
     * - 超过抛出以下异常 Exception in thread "main" org.springframework.core.task.TaskRejectedException: Executor [java.util.concurrent.ThreadPoolExecutor@7d156f8f[Running, pool size = 2, active threads = 2, queued tasks = 3, completed tasks = 0]] did not accept task: org.springframework.aop.interceptor.AsyncExecutionInterceptor$$Lambda$1302/0x00000008012129a0@54f25b42
     */
    public static final int queueCapacity = 3;

    @Bean("myExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("my-executor-");
        return executor;
    }

}
