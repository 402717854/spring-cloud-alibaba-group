package com.springcloud.alibaba.demo.openfegin.config;

import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfigure {

//    @Bean
//    public Retryer feignRetryer(){
//        // period=100 发起当前请求的时间间隔,单位毫秒
//        // maxPeriod=1000 发起当前请求的最大时间间隔,单位毫秒
//        // maxAttempts=2 重试次数是1，因为包括第一次，所以我们如果想要重试2次，就需要设置为3
//        Retryer retryer = new Retryer.Default(100, 1000, 2);
//        return retryer;
//    }
}
