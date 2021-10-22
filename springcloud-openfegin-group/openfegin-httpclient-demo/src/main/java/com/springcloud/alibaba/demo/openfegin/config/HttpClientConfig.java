package com.springcloud.alibaba.demo.openfegin.config;

import com.springcloud.alibaba.demo.openfegin.support.HttpClientProperties;
import feign.Feign;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnClass(Feign.class)
@EnableConfigurationProperties({HttpClientProperties.class})
@AutoConfigureBefore(FeignRibbonClientAutoConfiguration.class)
@Slf4j
public class HttpClientConfig {

    private final Timer connectionManagerTimer = new Timer(
            "FeignApacheHttpClientConfiguration.connectionManagerTimer", true);
    @Autowired(required = false)
    private RegistryBuilder registryBuilder;

    private CloseableHttpClient httpClient;

    @Bean
    public HttpClientConnectionManager connectionManager(
            ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
            HttpClientProperties httpClientProperties) {
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
                //关闭空闲连接  源码配置中没有设置空闲连接的关闭
                connectionManager.closeIdleConnections(httpClientProperties.getMaxIdleTime(),TimeUnit.MILLISECONDS);
            }
        }, 30000, httpClientProperties.getConnectionTimerRepeat());
        return connectionManager;
    }
    @Bean
    @ConditionalOnProperty(value = "feign.compression.response.enabled",
            havingValue = "true")
    public CloseableHttpClient customHttpClient(
            HttpClientConnectionManager httpClientConnectionManager,
            HttpClientProperties httpClientProperties) {
        HttpClientBuilder builder = HttpClientBuilder.create().disableCookieManagement()
                .useSystemProperties();
        //重试次数-默认关闭
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientProperties.getRetryCount(),httpClientProperties.isRequestSentRetryEnabled()));
        //初始化清理过期连接
        builder.evictExpiredConnections();
        //初始化清理空闲连接
        builder.evictIdleConnections(httpClientProperties.getMaxIdleTime(),TimeUnit.MILLISECONDS);
        //设置长连接保持策略
        builder.setKeepAliveStrategy(keepAliveStrategy(httpClientProperties));
        //设置默认请求头
        List<Header> headers = getDefaultHeaders();
        builder.setDefaultHeaders(headers);
        this.httpClient = createClient(builder, httpClientConnectionManager,
                httpClientProperties);
        return this.httpClient;
    }

    @Bean
    @ConditionalOnProperty(value = "feign.compression.response.enabled",
            havingValue = "false", matchIfMissing = true)
    public CloseableHttpClient httpClient(ApacheHttpClientFactory httpClientFactory,
                                          HttpClientConnectionManager httpClientConnectionManager,
                                          HttpClientProperties httpClientProperties) {
        HttpClientBuilder httpClientBuilder = httpClientFactory.createBuilder();
        this.httpClient = createClient(httpClientBuilder,
                httpClientConnectionManager, httpClientProperties);
        return this.httpClient;
    }

    private CloseableHttpClient createClient(HttpClientBuilder builder,
                                             HttpClientConnectionManager httpClientConnectionManager,
                                             HttpClientProperties httpClientProperties) {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientProperties.getConnectionTimeout())
                .setRedirectsEnabled(httpClientProperties.isFollowRedirects())
                .setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
                .build();
        CloseableHttpClient httpClient = builder
                .setDefaultRequestConfig(defaultRequestConfig)
                .setConnectionManager(httpClientConnectionManager).build();
        return httpClient;
    }

    /**
     * 配置长连接保持策略
     *
     * @return
     */
    public ConnectionKeepAliveStrategy keepAliveStrategy(HttpClientProperties httpClientProperties) {
        return (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            //从response读取长链接过期时间参数
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    try {
                        return Long.parseLong(value);
                    } catch (NumberFormatException ignore) {
                        log.error("从response解析长连接过期时间异常", ignore);
                    }
                }
            }
            //如果没有获取到，则使用默认长连接保持时间
            return httpClientProperties.getTimeToLive();
        };
    }
    /**
     * 设置请求头
     *
     * @return
     */
    private List<Header> getDefaultHeaders() {
        List<Header> headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
        headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
        headers.add(new BasicHeader("Accept-Language", "zh-CN"));
        headers.add(new BasicHeader("Connection", "Keep-Alive"));
        return headers;
    }

}
