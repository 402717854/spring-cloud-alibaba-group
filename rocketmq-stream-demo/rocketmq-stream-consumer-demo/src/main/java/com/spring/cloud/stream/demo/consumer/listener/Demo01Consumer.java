package com.spring.cloud.stream.demo.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.spring.cloud.stream.demo.consumer.message.Demo01Message;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Component;

@Component
public class Demo01Consumer {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //@Payload声明需要进行反序列化成 POJO 对象
    @StreamListener(MySink.ERBADAGANG_INPUT)
    public void onMessage(@Payload Demo01Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), JSON.toJSON(message));
        // <1> 注意，此处抛出一个 RuntimeException 异常，模拟消费失败
        throw new RuntimeException("我就是故意抛出一个异常");
    }

    @StreamListener(MySink.TREK_INPUT)
    public void onTrekMessage(@Payload Demo01Message message) {
        logger.info("[onMessage][线程编号:{} 消息内容：{}]", Thread.currentThread().getId(), message);
    }

    @ServiceActivator(inputChannel = "ERBADAGANG-TOPIC-01.erbadagang-consumer-group-ERBADAGANG-TOPIC-01.errors")
    public void handleError(ErrorMessage errorMessage) {
        Message<Demo01Message> originalMessage = (Message<Demo01Message>) errorMessage.getOriginalMessage();
        logger.error("[handleError][payload：{}]", ExceptionUtils.getRootCauseMessage(errorMessage.getPayload()));
        logger.error("[handleError][originalMessage：{}]", JSON.toJSON(originalMessage));
        logger.error("[handleError][headers：{}]", errorMessage.getHeaders());
    }
}
