package com.springcloud.alibaba.demo.openfegin.support;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;

@ConfigurationProperties(prefix = "feign.httpclient")
public class HttpClientProperties extends FeignHttpClientProperties {


    public static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 60000*60;

    public static final int DEFAULT_RETRY_COUNT = 1;
    //从连接池中获取连接的超时时间，超过该时间未拿到可用连接，会抛出org.apache.http.conn.ConnectionPoolTimeoutException
    private int connectionRequestTimeout = DEFAULT_CONNECTION_REQUEST_TIMEOUT;
    //重试次数
    private int retryCount= DEFAULT_RETRY_COUNT;
    //是否开启重试
    private boolean requestSentRetryEnabled =false;
    //保持连接最大空闲时间
    private long maxIdleTime=DEFAULT_CONNECTION_TIMER_REPEAT;

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public boolean isRequestSentRetryEnabled() {
        return requestSentRetryEnabled;
    }

    public void setRequestSentRetryEnabled(boolean requestSentRetryEnabled) {
        this.requestSentRetryEnabled = requestSentRetryEnabled;
    }

    public long getMaxIdleTime() {
        return maxIdleTime;
    }

    public void setMaxIdleTime(long maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }
}
