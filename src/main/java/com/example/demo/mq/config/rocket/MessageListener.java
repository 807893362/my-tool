package com.example.demo.mq.config.rocket;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.Iterator;
import java.util.List;

public class MessageListener  implements MessageListenerConcurrently {
    private MessageProcessor messageProcessor;

    public MessageListener() {
    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }

    @SneakyThrows
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        Iterator var3 = msgs.iterator();

        while(var3.hasNext()) {
            MessageExt msg = (MessageExt)var3.next();
            if (!this.messageProcessor.handleMessage(msg.getTags(), msg)) {
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        }

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}