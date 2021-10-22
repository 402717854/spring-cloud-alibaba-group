package com.springcloud.alibaba.demo.openfegin.controller;

import com.springcloud.alibaba.demo.openfegin.service.EchoService;
import com.springcloud.alibaba.demo.openfegin.utils.HttpUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class TestController {

    @Autowired
    private EchoService echoService;

    @Autowired
    private DiscoveryClient discoveryClient;

    @GetMapping("/notFound-feign")
    public String notFound() {
        return echoService.notFound();
    }

    @GetMapping("/divide-feign")
    public String divide(@RequestParam Integer a, @RequestParam Integer b) {
        return echoService.divide(a, b);
    }

    @GetMapping("/divide-feign2")
    public String divide(@RequestParam Integer a) {
        return echoService.divide(a);
    }

    @GetMapping("/echo-feign/{str}")
    public String feign(@PathVariable String str) {
        return echoService.echo(str);
    }

    @GetMapping("/services/{service}")
    public Object client(@PathVariable String service) {
        return discoveryClient.getInstances(service);
    }

    @GetMapping("/services")
    public Object services() {
        return discoveryClient.getServices();
    }
    @GetMapping("/divide-feign3")
    public String divide2(@RequestParam Integer a, @RequestParam Integer b) {
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try{
                        String divide = echoService.divide(a, b);
                        System.out.println(divide);
                    }catch (Exception e){
                        e.fillInStackTrace();
                    }
                }
            };
            thread.start();
        }
        return "请求完成";
    }
    @GetMapping("/divide-http")
    public String divide4(@RequestParam Integer a, @RequestParam Integer b) {
        String URL = "http://192.168.43.5:8082/divide?a="+a+"&b="+b;
        String result = HttpUtils.httpGet(URL, null);
        return result;
    }
}
