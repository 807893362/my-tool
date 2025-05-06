package com.example.demo.mq.config.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * kakfa监听器:
 * 1.继承 IKafkaListener
 * 2.创建 KafkaListenerContainerFactory
 * 3.实现 onMessage
 * 4.添加 注解 @KafkaListener(topics = "${topic}", containerFactory = "${KafkaListenerContainerFactory}")
 */
public interface IKafkaListener {

    void onMessage(ConsumerRecord<String, String> record);

}
