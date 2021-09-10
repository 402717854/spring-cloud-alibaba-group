package com.springcloud.alibaba.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringcloudAlibabaNacosProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringcloudAlibabaNacosProviderApplication.class, args);
    }
}
