####fegin默认重试次数为0
####关于调用微服务出现java.net.SocketTimeoutException: Read timed out

     原因一：消费者端如下配置设置参数低
        #请求的连接超时时长，单位：毫秒。默认为 10 * 1000 毫秒
        feign.client.config.default.connect-timeout=60000
        #请求的读取超时时长，单位：毫秒。默认为 60 * 1000 毫秒
        feign.client.config.default.read-timeout=60000
     原因二:服务提供者端如下配置参数不能满足当前业务需求
           请求在进行排队处理时会导致Read timed out
            #当所有可能的请求处理线程都在使用时，传入连接请求的最大队列长度。默认值100
            server.tomcat.accept-count=1
            #最大工作线程数 默认值200
            server.tomcat.threads.max=2
            #最小工作线程数 默认值10
            server.tomcat.threads.min-spare=2


###连接获取超时Timeout waiting for connection from pool 
         主要关系是:RequestConfig.connectionRequestTimeout 从连接池中获取连接的超时时间，超过该时间未拿到可用连接
         与以下配置有一定关系
         feign.httpclient.max-connections  整个连接池的总数量大小
         feign.httpclient.max-connections-per-route某一个服务每次能并行接收的请求数量
        服务1要通过Fluent调用服务2的接口。服务1发送了400个请求，但由于Fluent默认只支持maxPerRoute=100，MaxTotal=200，比如接口执行时间为500ms，
        由于maxPerRoute=100，所以要分为100,100,100,100分四批来执行，
        全部执行完成需要2000ms。而如果maxPerRoute设置为400，全部执行完需要500ms。在这种情况下（提供并发能力时）就要对这两个参数进行设置了。