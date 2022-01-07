package com.spring.cloud.stream.demo.producer.controller;

import com.spring.cloud.stream.demo.producer.message.Demo01Message;
import com.spring.cloud.stream.demo.producer.message.MySource;
import org.apache.rocketmq.common.message.MessageConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/demo01")
public class Demo01Controller {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MySource mySource;//<1>

    @GetMapping("/send")
    public boolean send() {
        System.out.println("开始时间:"+System.currentTimeMillis());
        // <2>创建 Message
        Demo01Message message=new Demo01Message();
        message.setId(new Random().nextInt());
        // <3>创建 Spring Message 对象
        Message<Demo01Message> springMessage = MessageBuilder.withPayload(message).setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL,7).build();
        // <4>发送消息
        return mySource.erbadagangOutput().send(springMessage);
    }

    @GetMapping("/sendTrek")
    public boolean sendTrek() {
        // <2>创建 Message
        Demo01Message message = new Demo01Message();
        message.setId(new Random().nextInt());
        // <3>创建 Spring Message 对象
        Message<Demo01Message> springMessage = MessageBuilder.withPayload(message)
                .build();
        // <4>发送消息
        return mySource.trekOutput().send(springMessage);
    }

    @GetMapping("/send_delay")
    public boolean sendDelay() {
        // 创建 Message
        Demo01Message message = new Demo01Message();
        message.setId(new Random().nextInt());
        // 创建 Spring Message 对象
        Message<Demo01Message> springMessage = MessageBuilder.withPayload(message)
                .setHeader(MessageConst.PROPERTY_DELAY_TIME_LEVEL, 6) // 设置延迟级别为 3，10 秒后消费。
                .build();
        // 发送消息
        boolean sendResult = mySource.erbadagangOutput().send(springMessage);
        logger.info("[sendDelay][发送消息完成, 结果 = {}]", sendResult);
        return sendResult;
    }

    @GetMapping("/send_tag")
    public boolean sendTag() {
        for (String tag : new String[]{"trek", "specialized", "look"}) {
            // 创建 Message
            Demo01Message message = new Demo01Message();
            message.setId(new Random().nextInt());
            // 创建 Spring Message 对象
            Message<Demo01Message> springMessage = MessageBuilder.withPayload(message)
                    .setHeader(MessageConst.PROPERTY_TAGS, tag) // 设置 Tag
                    .build();
            // 发送消息
            mySource.erbadagangOutput().send(springMessage);
        }
        return true;
    }

    public static void main(String[] args) {
        int i = new Random().nextInt();
        System.out.println(i);
    }
}
