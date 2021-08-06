package com.springcloud.alibaba.demo.controller;

import com.alibaba.csp.sentinel.adapter.apache.httpclient.SentinelApacheHttpClientBuilder;
import com.alibaba.csp.sentinel.adapter.apache.httpclient.config.SentinelApacheHttpClientConfig;
import com.alibaba.csp.sentinel.adapter.apache.httpclient.extractor.ApacheHttpClientResourceExtractor;
import com.alibaba.csp.sentinel.adapter.apache.httpclient.fallback.ApacheHttpClientFallback;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.SentinelRpcException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.springcloud.alibaba.demo.service.TestService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestWrapper;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ApacheHttpClientTestController {

    @RequestMapping("/httpclient/back")
    public String back() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("back");
        return "Welcome Back!";
    }

    @RequestMapping("/httpclient/back/{id}")
    public String back(@PathVariable String id) {
        if(!StringUtils.isEmpty(id)){
            int parseInt = Integer.parseInt(id);
            if(parseInt<10){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("back");
        return "Welcome Back! " + id;
    }
    @Autowired
    private TestService service;

    @GetMapping(value = "/hello/{name}")
    public String apiHello(@PathVariable String name) {
        return service.sayHello(name);
    }

    @RequestMapping("/httpclient/sync")
    public String sync() throws Exception {
        SentinelApacheHttpClientConfig config = new SentinelApacheHttpClientConfig();
        config.setExtractor(new ApacheHttpClientResourceExtractor() {
            @Override
            public String extractor(HttpRequestWrapper request) {
                String contains = "/httpclient/back/";
                String uri = request.getRequestLine().getUri();
                if (uri.startsWith(contains)) {
                    uri = uri.substring(0, uri.indexOf(contains) + contains.length()) + "{id}";
                }
                String resourceName=request.getMethod() + ":" + uri;
                return resourceName;
            }
        });
        config.setFallback(new ApacheHttpClientFallback() {
            @Override
            public CloseableHttpResponse handle(HttpRequestWrapper request, BlockException e) {
                if(e instanceof FlowException){
                    System.out.println("流量限制");
                }
                throw new SentinelRpcException(e);
            }
        });
        CloseableHttpClient httpclient = new SentinelApacheHttpClientBuilder(config).build();
        HttpGet httpGet = new HttpGet("http://localhost:8081/httpclient/back");
        return getRemoteString(httpclient, httpGet);
    }
    private static String getRemoteString(CloseableHttpClient httpclient, HttpGet httpGet) throws IOException {
        String result;
        HttpContext context = new BasicHttpContext();
        CloseableHttpResponse response;
        response = httpclient.execute(httpGet, context);
        try {
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        httpclient.close();
        return result;
    }
}
