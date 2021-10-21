package com.springcloud.alibaba.demo.openfegin.config;

import com.springcloud.alibaba.demo.openfegin.support.FeignOKHttpClientProperties;
import feign.Feign;
import okhttp3.ConnectionPool;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@EnableConfigurationProperties({ FeignOKHttpClientProperties.class })
@AutoConfigureBefore(FeignRibbonClientAutoConfiguration.class)
public class OKHttpClientConfig {
    @Bean
    public okhttp3.OkHttpClient okHttpClient(FeignOKHttpClientProperties feignOKHttpClientProperties){
        return new okhttp3.OkHttpClient.Builder()
                //设置连接超时
                .connectTimeout(feignOKHttpClientProperties.getConnectTimeout(), TimeUnit.MILLISECONDS)
                //设置读超时
                .readTimeout(feignOKHttpClientProperties.getReadTimeout(), TimeUnit.MILLISECONDS)
                //设置写超时
                .writeTimeout(feignOKHttpClientProperties.getWriteTimeout(),TimeUnit.MILLISECONDS)
                //是否自动重连
                .retryOnConnectionFailure(feignOKHttpClientProperties.isRetryOnConnectionFailure())
                .connectionPool(new ConnectionPool(feignOKHttpClientProperties.getMaxIdleConnections(),feignOKHttpClientProperties.getKeepAliveDuration(),TimeUnit.MILLISECONDS))
                //构建OkHttpClient对象
                .build();
    }
}
