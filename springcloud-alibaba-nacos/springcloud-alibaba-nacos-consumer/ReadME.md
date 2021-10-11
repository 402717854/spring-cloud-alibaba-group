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