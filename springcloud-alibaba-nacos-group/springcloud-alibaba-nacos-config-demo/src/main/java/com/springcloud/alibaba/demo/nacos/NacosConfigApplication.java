package com.springcloud.alibaba.demo.nacos;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;
import java.util.concurrent.Executor;

@SpringBootApplication
public class NacosConfigApplication {
    public static void main(String[] args) {
        SpringApplication.run(NacosConfigApplication.class,args);

        String serverAddr = "192.168.66.9:8848";
        String dataId = "thread-demo.yaml";
        String group = "SSM-DEMO-GROUP";
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
        properties.put(PropertyKeyConst.NAMESPACE, "9739d612-297a-4a7c-b098-6090edf88dea");
        ConfigService configService = null;
        try {
            configService = NacosFactory.createConfigService(properties);
            String content = configService.getConfig(dataId, group, 5000);
            System.out.println(content);
            //注册监听
            configService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    System.out.println(configInfo);
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
