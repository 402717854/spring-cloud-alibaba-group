package com.springcloud.alibaba.demo.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class PingController {

    @RequestMapping("/")
    public void ping(){
        log.info("ping 127.0.0.1:8084 success");
    }
}
