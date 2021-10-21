###HTTP Client 替换替换feign默认Client
    
        添加依赖
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-httpclient</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
        </dependency>
        配置
        feign:  #HttpClientFeignLoadBalancedConfiguration FeignHttpClientProperties
            httpclient:
                enabled: true
                max-connections: 200 #最大连接数 默认值200
                max-connections-per-route: 50 #最大路由 默认值50
            #不配置会拿RibbonClientConfiguration。ribbonClientConfig()默认值
            #不配置feign默认配置会进行重试，但是具体走哪一个重试机制未知(待验证)
            client:
                config:
                    default:
                        read-timeout: 60000 #请求的读取超时时长，单位：毫秒。默认为 60 * 1000 毫秒
                        connect-timeout: 5000 #请求的连接超时时长，单位：毫秒。默认为 10 * 1000 毫秒