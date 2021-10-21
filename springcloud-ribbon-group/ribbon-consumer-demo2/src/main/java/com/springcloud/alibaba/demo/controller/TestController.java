package com.springcloud.alibaba.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TestController {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RestTemplate restTemplate2;
    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @GetMapping("/divide")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        // 获得服务 `demo-provider` 的一个实例
        ServiceInstance instance = loadBalancerClient.choose("nacos-provider");
        // 发起调用
        String targetUrl = instance.getUri() + "/divide?a=" + a+"&b="+b;
        String response = restTemplate2.getForObject(targetUrl, String.class);
        // 返回结果
        return "consumer:" + response;
    }

    @GetMapping("/divide2")
    public String divide2(@RequestParam Integer a, @RequestParam Integer b) {
        // 直接使用 RestTemplate 调用服务 `demo-provider`
        String targetUrl = "http://nacos-provider/divide?a=" + a+"&b="+b;
        String response = restTemplate.getForObject(targetUrl, String.class);
        // 返回结果
        return "consumer:" + response;
    }
}
