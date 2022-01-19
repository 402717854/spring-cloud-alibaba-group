package com.spring.cloud.stream.demo.producer;

import com.spring.cloud.stream.demo.producer.message.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author lenovo
 */
@SpringBootApplication
@EnableBinding(MySource.class)//声明指定接口开启 Binding 功能，扫描其 @Input 和 @Output 注解
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
    }
}
