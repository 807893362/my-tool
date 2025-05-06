package com.example.demo.mq.config.rocket.config;

import com.example.demo.mq.config.rocket.exception.RocketMqException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;

public class RocketMqProducerConfiguration {

    @SneakyThrows
    public static DefaultMQProducer initMQProducer(String syncGroupName, String namesrvAddr, int maxMessageSize, int sendMsgTimeout) throws RocketMqException {
        if (StringUtils.isBlank(syncGroupName)) {
            throw new RocketMqException("syncGroupName is blank");
        } else if (StringUtils.isBlank(namesrvAddr)) {
            throw new RocketMqException("namesrvAddr is blank");
        } else {
            DefaultMQProducer producer = new DefaultMQProducer(syncGroupName);
            producer.setNamesrvAddr(namesrvAddr);
            producer.setRetryTimesWhenSendFailed(16);
            producer.setMaxMessageSize(maxMessageSize);
            producer.setSendMsgTimeout(sendMsgTimeout);
            producer.start();
            return producer;
        }
    }
}