####ribbon高可用调用http服务(不使用注册中心)
    
    自定义高可用负载均衡器com.springcloud.alibaba.demo.hystrix.ribbon.rule.HighAvailabilityServerRule
    通过定时队列是否能够ping通http服务提供者，将存活的服务提供者放到存活服务列表中，通过轮询的方式获取存活服务

###ribbon的ConnectTimeout\ReadTimeout参数配置多加留意