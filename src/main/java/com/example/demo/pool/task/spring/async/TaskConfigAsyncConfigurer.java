package com.example.demo.pool.task.spring.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Executor;

/**
 * 两种方式：
 * implements AsyncConfigurer
 * extends AsyncConfigurerSupport
 */
//@Configuration
@Slf4j
public class TaskConfigAsyncConfigurer implements AsyncConfigurer {

    @Bean("asyncConfigurerExecutor")
    public ThreadPoolTaskExecutor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(TaskConfig.corePoolSize);
        executor.setMaxPoolSize(TaskConfig.maxPoolSize);
        executor.setQueueCapacity(TaskConfig.queueCapacity);
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setThreadNamePrefix("asyncConfigurer-executor-");
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return executor();
    }

    /**
     * 只处理异常，不关心线程池大小，溢出问题
     */
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
