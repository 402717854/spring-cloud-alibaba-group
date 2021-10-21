package com.springcloud.alibaba.demo.openfegin.config;

import feign.Feign;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@EnableConfigurationProperties({ FeignHttpClientProperties.class })
@AutoConfigureBefore(FeignRibbonClientAutoConfiguration.class)
public class HttpClientConfig {

    private final Timer connectionManagerTimer = new Timer(
            "FeignApacheHttpClientConfiguration.connectionManagerTimer", true);
    @Autowired(required = false)
    private RegistryBuilder registryBuilder;
    @Bean
    public HttpClientConnectionManager connectionManager(
            ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
            FeignHttpClientProperties httpClientProperties) {
        final HttpClientConnectionManager connectionManager = connectionManagerFactory
                .newConnectionManager(httpClientProperties.isDisableSslValidation(),
                        httpClientProperties.getMaxConnections(),
                        httpClientProperties.getMaxConnectionsPerRoute(),
                        httpClientProperties.getTimeToLive(),
                        httpClientProperties.getTimeToLiveUnit(), this.registryBuilder);
        this.connectionManagerTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                //关闭过期连接
                connectionManager.closeExpiredConnections();
                //关闭空闲连接
                connectionManager.closeIdleConnections(1,TimeUnit.SECONDS);
            }
        }, 30000, httpClientProperties.getConnectionTimerRepeat());
        return connectionManager;
    }
}
