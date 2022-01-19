package com.springcloud.alibaba.demo.hystrix.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hystrix-user-service",url = "127.0.0.1:8084", fallbackFactory = UserServiceFeignClientFallbackFactory.class)
public interface UserServiceFeignClient {

    @GetMapping("/user/get")
    String getUser(@RequestParam("id") Integer id);

}
