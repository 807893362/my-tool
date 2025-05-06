package com.example.demo.mq.config.rocket;

import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageProcessor {
    boolean handleMessage(String tags, MessageExt msg);
}
