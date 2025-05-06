package com.example.demo.mq.test;

import com.alibaba.fastjson2.JSONObject;
import com.example.demo.mq.config.rocket.MessageListener;
import com.example.demo.mq.config.rocket.MessageProcessor;
import com.example.demo.mq.config.rocket.config.RocketMqProducerConfiguration;
import com.example.demo.mq.config.rocket.exception.RocketMqException;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableKafka
public class RocketTest {

    // 消费者 & 生产者
    private static final String ROCKET_TEST_GROUP_P = "MyTestGroupP";
    private static final String ROCKET_TEST_GROUP_C = "MyTestGroupC";
    // 消费者初始化需要指定,生产者发送消息时指定
    private static final String ROCKET_TEST_TOPIC = "MyTestTopic";
    private static final String ROCKET_TEST_TAG_A = "MyTestTagA";
    private static final String ROCKET_TEST_TAG_B = "MyTestTagB";
    private static final String ROCKET_TEST_TAG_C = "MyTestTagC";
    // 生产者
    private static final int ROCKET_MAX_MESSAGE_SIZE=26214400;
    private static final int ROCKET_SEND_MSG_TIMEOUT=10000; //默认3秒超时
    // 消费者
    private static final int ROCKET_CONSUMER_THREAD_MIN=3;
    private static final int ROCKET_CONSUMER_THREAD_MAX=6;

    @SneakyThrows
    public static void main(String[] args) {
        ConfigurableApplicationContext application = SpringApplication.run(RocketTest.class, args);
        DefaultMQProducer producer = (DefaultMQProducer) application.getBean("rocketTestProducer");

        long officialId = 1L;
        while (true){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("officialId", officialId); // 官方账号uid
            Message sendMsg = new Message(ROCKET_TEST_TOPIC, ROCKET_TEST_TAG_A, String.valueOf(officialId).getBytes());
            SendResult sendResult = producer.send(sendMsg);
            System.err.format("rocket send%s future=>%s \n", officialId, sendResult);

//            sendMsg = new Message(ROCKET_TEST_TOPIC, ROCKET_TEST_TAG_C, String.valueOf(officialId).getBytes());
//            sendResult = producer.send(sendMsg);
//            System.err.format("rocket send%s future=>%s \n", officialId, sendResult);
            officialId++;
            TimeUnit.SECONDS.sleep(5);
        }

    }

    @Component
    public class RocketListenerTest implements MessageProcessor {
        @Override
        public boolean handleMessage(String tags, MessageExt msg) {
            System.err.format("rocket listen%s record=>%s \n", new String(msg.getBody()), msg);
            return Boolean.TRUE;
        }

    }

    @Component
    class RocketProducerTest{
        @Value("${rocketmq.namesrvAddr}")
        private String namesrvAddr;

        @Bean
        @SneakyThrows
        private DefaultMQProducer rocketTestProducer(){
            return RocketMqProducerConfiguration.initMQProducer(ROCKET_TEST_GROUP_P, namesrvAddr, ROCKET_MAX_MESSAGE_SIZE, ROCKET_SEND_MSG_TIMEOUT);
        }
    }

    @Configuration
    class RocketMqConsumerConfiguration {
        @Value("${rocketmq.namesrvAddr}")
        private String namesrvAddr;

        @Autowired
        private MessageProcessor messageProcessor;

        public RocketMqConsumerConfiguration() {
        }

        @Bean
        @SneakyThrows
        public DefaultMQPushConsumer getRocketMQConsumer() {
            if (StringUtils.isBlank(this.namesrvAddr)) {
                throw new RocketMqException("namesrvAddr is null !!!");
            } else {
                DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(ROCKET_TEST_GROUP_C);
                consumer.setNamesrvAddr(this.namesrvAddr);
                consumer.setMessageModel(MessageModel.BROADCASTING);
                consumer.setConsumeThreadMin(ROCKET_CONSUMER_THREAD_MIN);
                consumer.setConsumeThreadMax(ROCKET_CONSUMER_THREAD_MAX);
                consumer.setConsumeMessageBatchMaxSize(32);
//                consumer.setMessageModel(MessageModel.BROADCASTING);
//                consumer.setMessageModel(MessageModel.CLUSTERING);
                MessageListener messageListener = new MessageListener();
                messageListener.setMessageProcessor(this.messageProcessor);
                consumer.registerMessageListener(messageListener);
                StringBuilder tag = new StringBuilder();
                tag.append(ROCKET_TEST_TAG_A).append("||").append(ROCKET_TEST_TAG_B);
                consumer.subscribe(ROCKET_TEST_TOPIC, tag.toString());
                consumer.start();
                return consumer;
            }
        }
    }


}

