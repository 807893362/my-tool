package com.example.demo.pool.task.spring.async;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

/**
 * 必须添加注解 @EnableAsync 开启异步注解
 * - 验证 checkCorePool
 * - 验证 checkMaxPool
 * - 验证 checkQueueCapacity
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class AsyncTest {

    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(AsyncTest.class, args);
        AsyncService asyncService= (AsyncService) application.getBean("asyncService");

        // 带参 无返回值
        asyncService.getX(5);
        // 带参 有返回值 -> AopInvocationException: Null return value from advice does not match primitive return type for: xxx
        System.err.println(asyncService.getSetX(10));

//        checkCorePool(asyncService);
//        checkMaxPool(asyncService);
//        checkQueueCapacity(asyncService);
    }

    public static void checkCorePool(AsyncService asyncService){

        for (int i = 0; i <= TaskConfig.corePoolSize; i++) {
            asyncService.get();
            System.err.println("get():" + new Date());
        }
//        get():Tue Sep 19 11:31:48 CST 2023
//        get():Tue Sep 19 11:31:48 CST 2023
//        AsyncService :Tue Sep 19 11:31:53 CST 2023
//        AsyncService :Tue Sep 19 11:31:58 CST 2023
    }

    public static void checkMaxPool(AsyncService asyncService){
        for (int i = 0; i <= TaskConfig.maxPoolSize; i++) {
            asyncService.get();
            System.err.println("get():" + new Date());
        }
//       get():Tue Sep 19 11:32:19 CST 2023
//       get():Tue Sep 19 11:32:19 CST 2023
//       AsyncService :Tue Sep 19 11:32:24 CST 2023
//       AsyncService :Tue Sep 19 11:32:29 CST 2023
//       AsyncService :Tue Sep 19 11:32:34 CST 2023
    }

    public static void checkQueueCapacity(AsyncService asyncService){
        int size = TaskConfig.corePoolSize + 2;
        for (int i = 0; i <= TaskConfig.queueCapacity + size; i++) {
            asyncService.get();
            System.err.println("get():" + new Date());
        }
//        get():Tue Sep 19 11:34:09 CST 2023
//        get():Tue Sep 19 11:34:09 CST 2023
//        get():Tue Sep 19 11:34:09 CST 2023
//        get():Tue Sep 19 11:34:09 CST 2023
//        get():Tue Sep 19 11:34:09 CST 2023
//        AsyncService :Tue Sep 19 11:34:14 CST 2023
//        AsyncService :Tue Sep 19 11:34:14 CST 2023
//        AsyncService :Tue Sep 19 11:34:19 CST 2023
//        AsyncService :Tue Sep 19 11:34:19 CST 2023
//        AsyncService :Tue Sep 19 11:34:24 CST 2023
    }

}
