package com.springcloud.alibaba.demo.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("undertow")
public class UndertowServerController {

    @RequestMapping("/test")
    public String test(){
        String str="欢迎测试UndertowServer吞吐量";
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  str;
    }
}
