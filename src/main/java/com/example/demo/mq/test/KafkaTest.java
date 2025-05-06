package com.example.demo.mq.test;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.mq.config.kafka.IKafkaListener;
import com.example.demo.mq.config.kafka.KafkaTemplateWrapper;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@SpringBootApplication
@EnableKafka
//spring.main.allow-circular-references:true
public class KafkaTest {

    private static final String KAFKA_TEST_TOPIC = "zeroRateAlarm";
    private static final String KAFKA_TEST_GROUP = "testGroup";

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(KafkaTest.class, args);
        KafkaTemplateWrapper wrapper = (KafkaTemplateWrapper) application.getBean("kafkaTestProducer");

        long officialId = 1L;
        while (true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("officialId", officialId); // 官方账号uid
            ListenableFuture future = wrapper.send(String.valueOf(officialId), jsonObject.toString());
            Object o = future.get();
            System.err.format("kafka send%s future=>%s \n", officialId, o.toString());
            officialId ++;
            TimeUnit.SECONDS.sleep(5);
        }

    }

    @Component
    class KafkaListenerTest implements IKafkaListener {

        public KafkaListenerTest() {
            System.err.println("KafkaListenerTest init");
        }

        @Override
        @KafkaListener(topics = KAFKA_TEST_TOPIC, containerFactory = "consumerContainerFactory")
        @SneakyThrows
        public void onMessage(ConsumerRecord<String, String> record) {
            System.err.format("kafka listen%s record=>%s \n", record.key(), record);
        }
    }

    @Configurable
    class ConsumerKafkaConfig{

        @Value("${test.kafka.broker.list}")
        private String testKafkaServers;

        // consumer
        @Bean("consumerContainerFactory")
        public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, byte[]>> consumerContainerFactory() {
            ConcurrentKafkaListenerContainerFactory<String, byte[]> factory = new ConcurrentKafkaListenerContainerFactory<>();
            factory.setConsumerFactory(consumerFactory());
            return factory;
        }

        public ConsumerFactory<String, byte[]> consumerFactory() {
            return new DefaultKafkaConsumerFactory(consumerProperties(), new StringDeserializer(), new StringDeserializer());
        }

        private Map<String, Object> consumerProperties() {
            Map<String, Object> props = new HashMap<>();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, testKafkaServers);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_TEST_GROUP);
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }

    }

    @Configuration
    class KafkaProducerTest {
        @Qualifier("testKafkaTemplate")
        @Autowired
        private KafkaTemplate testKafkaTemplate;

        @Bean(name = "kafkaTestProducer")
        public KafkaTemplateWrapper kafkaTestProducer() {
            KafkaTemplateWrapper wrapper = new KafkaTemplateWrapper();
            wrapper.setKafkaTemplate(testKafkaTemplate);
            wrapper.setTopic(KAFKA_TEST_TOPIC);
            return wrapper;
        }
    }

    @Configuration
    class ProducerKafkaConfig {

        @Value("${test.kafka.broker.list}")
        private String testKafkaServers;

        @Bean(name = "testKafkaTemplate")
        public KafkaTemplate<String, String> testKafkaTemplate() {
            return new KafkaTemplate<>(producerFactory());
        }

        private ProducerFactory<String, String> producerFactory() {
            return new DefaultKafkaProducerFactory<>(producerConfigs());
        }

        private Map<String, Object> producerConfigs() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, testKafkaServers);
            props.put(ProducerConfig.RETRIES_CONFIG, 0);
            props.put(ProducerConfig.ACKS_CONFIG, "all"); // 不能写成 1
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            return props;
        }
    }

}

