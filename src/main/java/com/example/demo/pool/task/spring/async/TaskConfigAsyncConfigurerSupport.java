package com.example.demo.pool.task.spring.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 两种方式：
 * implements AsyncConfigurer
 * extends AsyncConfigurerSupport
 */
@Configuration
@Slf4j
public class TaskConfigAsyncConfigurerSupport extends AsyncConfigurerSupport {

    @Bean("asyncConfigurerSupportExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TaskConfig.corePoolSize);
        executor.setMaxPoolSize(TaskConfig.maxPoolSize);
        executor.setQueueCapacity(TaskConfig.queueCapacity);
        // 当调度器shutdown被调用时等待当前被调度的任务完成
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("asyncConfigurerSupport-executor-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
        // 对拒绝task的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncUncaughtExceptionHandler();
    }

    /**
     * 自定义异步异常处理器
     */
    static class CustomAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable ex, Method method, Object... params) {
            //此demo只打印日志，实际项目以具体业务需求来处理
            log.error(">>> CustomAsyncUncaughtExceptionHandler,class:{}, method: {}, params: {}, error: {}",
                    method.getDeclaringClass().getSimpleName(), method.getName(), Arrays.toString(params),
                    ex.getMessage());
        }
    }

}
