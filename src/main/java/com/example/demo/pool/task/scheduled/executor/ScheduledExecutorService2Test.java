package com.example.demo.pool.task.scheduled.executor;

import com.example.demo.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ScheduledExecutorService2Test {

    // 3s 执行一次
    public static final long CHECK_INTERVAL_TIME = 3 * 1000;

    /**
     * 自动处理游戏任务,每隔30s 检查一次是否有到时间的任务
     */
    ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void start() {
        scheduledExecutorService = ThreadPoolUtils.scheduleAtFixedRate(() -> process(), CHECK_INTERVAL_TIME);
    }

    @PreDestroy
    public void destroy(){
        scheduledExecutorService.shutdown();
    }

    /**
     * 处理核心业务
     */
    public void process() {
        long l = System.currentTimeMillis();
        int count = 1;

        System.err.format("process2 %s:%s \n", count, new Date());
        count++;
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
