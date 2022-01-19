package com.springcloud.alibaba.demo;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewaySentinelDemoApplication {
    public static void main(String[] args) {
        System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY, "1"); // 【重点】设置应用类型为 Spring Cloud Gateway
        SpringApplication.run(GatewaySentinelDemoApplication.class, args);
    }
}
