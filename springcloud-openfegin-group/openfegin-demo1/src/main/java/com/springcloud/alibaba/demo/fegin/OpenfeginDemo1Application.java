package com.springcloud.alibaba.demo.fegin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient(autoRegister = true)
@EnableFeignClients
public class OpenfeginDemo1Application {
    public static void main(String[] args) {
        SpringApplication.run(OpenfeginDemo1Application.class, args);
    }
}
