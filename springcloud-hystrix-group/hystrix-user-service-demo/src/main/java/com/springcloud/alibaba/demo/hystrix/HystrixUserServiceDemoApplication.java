package com.springcloud.alibaba.demo.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HystrixUserServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HystrixUserServiceDemoApplication.class, args);
    }
}
