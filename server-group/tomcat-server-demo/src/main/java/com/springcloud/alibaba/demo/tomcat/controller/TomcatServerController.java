package com.springcloud.alibaba.demo.tomcat.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("tomcat")
public class TomcatServerController {

    @RequestMapping("/test")
    public String test(){
        String str="欢迎测试tomcatServer吞吐量";
        try {
            byte[] bytes=new byte[512*1024];
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  str;
    }
}
