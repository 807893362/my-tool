package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
//@EnableDubbo(scanBasePackages={"com.asiainno.uplive.filter", "com.example.demo"})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(DemoApplication.class);
        application.run(args);
        Logger logger = LoggerFactory.getLogger(DemoApplication.class);
        logger.info("=================SpringBootApplication已启动==================");
    }
}
