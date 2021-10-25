package com.springcloud.alibaba.demo.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HystrixUserServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixUserServiceDemoApplication.class, args);
    }
}
