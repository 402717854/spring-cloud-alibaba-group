package com.springcloud.alibaba.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudAlibabaNacosProvider2Application {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudAlibabaNacosProvider2Application.class, args);
    }
}
