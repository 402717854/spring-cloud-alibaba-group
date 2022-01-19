package com.springcloud.alibaba.demo.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.springcloud.alibaba.demo.hystrix.service.UserServiceFeignClient;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/hystrix")
public class FeignDemoController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserServiceFeignClient userServiceFeignClient;

    @GetMapping("/fegin/get_user")
    public String getUser(@RequestParam("id") Integer id) {
        logger.info("[getUser][准备调用 user-service 获取用户({})详情]", id);
        return userServiceFeignClient.getUser(id);
    }

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/ribbon/get_user")
    @HystrixCommand(fallbackMethod = "getUserFallback")
    public String getUser2(@RequestParam("id") Integer id) {
        logger.info("[getUser][准备调用 user-service 获取用户({})详情]", id);
        ServiceInstance serviceInstance = loadBalancerClient.choose("hystrix-user-service");
        // 发起调用
        String targetUrl = serviceInstance.getUri() + "/user/get?id=" + id;
        return restTemplate.getForEntity(targetUrl, String.class).getBody();
    }
    public String getUserFallback(Integer id, Throwable throwable) {
        logger.info("[getUserFallback][id({}) exception({})]", id, ExceptionUtils.getRootCauseMessage(throwable));
        return "mock:User:" + id;
    }
}
