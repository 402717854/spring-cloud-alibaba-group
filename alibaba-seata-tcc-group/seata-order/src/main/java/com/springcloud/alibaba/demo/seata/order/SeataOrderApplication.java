package com.springcloud.alibaba.demo.seata.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@MapperScan("com.springcloud.alibaba.demo.seata.order.domain.order.repository")
@EnableFeignClients(basePackages = {"com.springcloud.alibaba.demo.seata.common.client"})
@EnableDiscoveryClient
public class SeataOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SeataOrderApplication.class, args);
    }

}
