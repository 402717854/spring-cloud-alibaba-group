package com.springcloud.alibaba.demo.openfegin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
@EnableFeignClients
public class OpenfeginHttpclientDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenfeginHttpclientDemoApplication.class, args);
    }
}
