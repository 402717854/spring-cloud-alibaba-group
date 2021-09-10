package com.springcloud.alibaba.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.springcloud.alibaba.demo.base.HttpClient4Test;
import com.springcloud.alibaba.demo.base.Params;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URL;

@RestController
public class NacosTestApiController extends HttpClient4Test{

    private int port=8848;

    @RequestMapping("/nacos/metrics")
    public void metrics() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        this.base = new URL(url);

        ResponseEntity<String> response = request("/nacos/v1/ns/operator/metrics",
                Params.newParams()
                        .done(),
                String.class);

        JSONObject json = (JSONObject) JSON.toJSON(response.getBody());
        System.out.println(json);
    }
}
