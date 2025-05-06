package com.example.demo.pool.task.scheduled.executor;

import com.example.demo.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;

/**
 * ScheduleAtFixedRate 每次执行时间为上一次任务开始起向后推一个时间间隔。
 * process 1:Tue Sep 19 10:26:50 CST 2023
 * process2 1:Tue Sep 19 10:26:50 CST 2023
 * process 1:Tue Sep 19 10:26:53 CST 2023
 * process2 1:Tue Sep 19 10:26:53 CST 2023
 *
 * ScheduleWithFixedDelay 不管任务command执行的时间是多长，下一次任务的执行时间都是上一次任务执行完后在等待延迟间隔delay时间后执行下一次任务。
 * process 1:Tue Sep 19 10:25:35 CST 2023
 * process2 1:Tue Sep 19 10:25:35 CST 2023
 * process 1:Tue Sep 19 10:25:38 CST 2023
 * process2 1:Tue Sep 19 10:25:41 CST 2023
 */
@Service
@Slf4j
public class ScheduledExecutorServiceTest {

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

        System.err.format("process %s:%s \n", count, new Date());
        count++;
    }



}
