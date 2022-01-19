package com.springcloud.alibaba.demo.fegin.controller;

import com.springcloud.alibaba.demo.fegin.service.EchoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private EchoService echoService;

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
}
