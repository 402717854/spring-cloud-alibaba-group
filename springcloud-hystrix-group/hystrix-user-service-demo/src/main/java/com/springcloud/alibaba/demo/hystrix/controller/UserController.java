package com.springcloud.alibaba.demo.hystrix.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("get")
    public String get(@RequestParam Integer id){
        return "User:"+id;
    }

    @GetMapping("batch_get")
    public List<String> batchGet(@RequestParam("ids") List<Integer> ids){
        return ids.stream().map(id->"USER:"+id).collect(Collectors.toList());
    }
}
