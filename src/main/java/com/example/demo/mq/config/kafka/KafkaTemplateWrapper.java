package com.example.demo.mq.config.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.lang.Nullable;
import org.springframework.util.concurrent.ListenableFuture;

public class KafkaTemplateWrapper<K, V> {
    private KafkaTemplate<K, V> kafkaTemplate;
    private String topic;

    public void setKafkaTemplate(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ListenableFuture<SendResult<K, V>> send(K key, @Nullable V data) {
        return kafkaTemplate.send(topic, key, data);
    }
}
