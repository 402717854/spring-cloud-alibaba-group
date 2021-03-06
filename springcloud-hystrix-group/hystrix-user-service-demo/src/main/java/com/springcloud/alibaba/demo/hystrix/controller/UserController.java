package com.springcloud.alibaba.demo.hystrix.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @GetMapping("get")
    public String get(@RequestParam Integer id){
        log.info("进入port==>8083");
//        try {
//            Thread.sleep(6000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return "进入port==>8083,User:"+id;
    }

    @GetMapping("batch_get")
    public List<String> batchGet(@RequestParam("ids") List<Integer> ids){
        return ids.stream().map(id->"USER:"+id).collect(Collectors.toList());
    }
}
