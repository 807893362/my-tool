package com.example.demo.pool.task.spring.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 *  @Async
 *  - 可以传递参数
 *  - 无法相应参数
 */
@Service
public class AsyncService {

//    @Async("myExecutor")
//    @Async("asyncConfigurerExecutor")
    @Async("asyncConfigurerSupportExecutor")
    public void get(){
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.err.println("AsyncService :" + new Date());
        System.err.println("ex" + 1/0);
    }


    @Async("myExecutor")
    public void getX(int x){
        System.err.println("parms:" + x);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.err.println("AsyncService :" + new Date());
    }

    @Async("myExecutor")
    public int getSetX(int x){
        System.err.println("parms:" + x);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.err.println("AsyncService :" + new Date());
        return new Random().nextInt();
    }


}
