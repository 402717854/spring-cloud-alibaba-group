package com.springcloud.alibaba.demo.hystrix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@SpringBootApplication
@EnableHystrixDashboard // 声明开启 Hystrix Dashboard 功能
@EnableDiscoveryClient
@EnableTurbine // 声明开启 Turbine 功能
public class HystrixDashboardApplication2 {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDashboardApplication2.class, args);
    }

}
