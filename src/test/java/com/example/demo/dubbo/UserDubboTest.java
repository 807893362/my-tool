package com.example.demo.dubbo;

import com.asiainno.uplive.room.service.IUserDubbo;
import com.example.demo.DemoApplication;
import org.apache.dubbo.config.annotation.DubboReference;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = DemoApplication.class)
public class UserDubboTest {

    @DubboReference
    private IUserDubbo userDubbo;

    @Test
    void getMobilePhoneTest(){
        for (int i = 0; i < 100; i++) {
            long l = System.currentTimeMillis();
            userDubbo.getMobilePhone("");
            System.out.format("%s, 耗时：%s.ms", i, (System.currentTimeMillis() - l));
            System.out.println("");
        }
    }


}
