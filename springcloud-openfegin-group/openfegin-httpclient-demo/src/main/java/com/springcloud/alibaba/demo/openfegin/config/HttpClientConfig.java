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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

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
                //??????????????????
                connectionManager.closeExpiredConnections();
                //??????????????????  ????????????????????????????????????????????????
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
        //????????????-????????????
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler(httpClientProperties.getRetryCount(),httpClientProperties.isRequestSentRetryEnabled()));
        //???????????????????????????
        builder.evictExpiredConnections();
        //???????????????????????????
        builder.evictIdleConnections(httpClientProperties.getMaxIdleTime(),TimeUnit.MILLISECONDS);
        //???????????????????????????
        builder.setKeepAliveStrategy(keepAliveStrategy(httpClientProperties));
        //?????????????????????
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

    @Bean
    public RestTemplate restTemplate(@Qualifier("clientHttpRequestFactory") ClientHttpRequestFactory clientHttpRequestFactory) {
        return new RestTemplate(clientHttpRequestFactory);
    }

    @Bean(name = "clientHttpRequestFactory")
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient);
    }

    /**
     * ???????????????????????????
     *
     * @return
     */
    private ConnectionKeepAliveStrategy keepAliveStrategy(HttpClientProperties httpClientProperties) {
        return (response, context) -> {
            HeaderElementIterator it = new BasicHeaderElementIterator(
                    response.headerIterator(HTTP.CONN_KEEP_ALIVE));
            //???response?????????????????????????????????
            while (it.hasNext()) {
                HeaderElement he = it.nextElement();
                String param = he.getName();
                String value = he.getValue();
                if (value != null && "timeout".equalsIgnoreCase(param)) {
                    try {
                        return Long.parseLong(value);
                    } catch (NumberFormatException ignore) {
                        log.error("???response?????????????????????????????????", ignore);
                    }
                }
            }
            //????????????????????????????????????????????????????????????
            return httpClientProperties.getTimeToLive();
        };
    }
    /**
     * ???????????????
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
