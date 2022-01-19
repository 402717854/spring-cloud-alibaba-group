package com.springcloud.alibaba.demo.openfegin.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class HttpUtils {
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final String CHARSET = "UTF-8";
    public static String httpPost(String url, String jsonArg, Map<String,String> header) {
        HttpPost httppost = new HttpPost(url);
        if(StringUtils.isNotBlank(jsonArg)){
            HttpEntity httpEntity = new StringEntity(jsonArg, CHARSET);
            httppost.setEntity(httpEntity);
        }
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httppost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        httppost.setHeader("Content-Type", "application/json");
        log.info("request url:{},request headers:{},request body:{}", url,
                Lists.newArrayList(httppost.getAllHeaders()).toString(), jsonArg);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    String result = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    return result;
                }
            }
            httppost.abort();
        } catch (IOException e) {
            log.error("response stream exception,{}", e);
        }finally {
            System.out.println("===========关闭响应============");
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    public static String httpGet(String url,Map<String,String> header){
        HttpGet httpGet = new HttpGet(url);
        if (header != null) {
            for (Map.Entry<String, String> entry : header.entrySet()) {
                httpGet.setHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setConnectionRequestTimeout(20000)
                .setSocketTimeout(60000)
                .build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Content-Type", "text/html;charset=UTF-8");
        log.info("request url:{},request headers:{}", url,
                Lists.newArrayList(httpGet.getAllHeaders()).toString());
        CloseableHttpResponse response=null;
        try {
            response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                if(entity!=null){
                    String result = EntityUtils.toString(entity);
                    EntityUtils.consume(entity);
                    return result;
                }
            }
            httpGet.abort();
        } catch (IOException e) {
            log.error("response stream exception,{}", e);
        } finally {
            System.out.println("===========关闭响应============");
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
