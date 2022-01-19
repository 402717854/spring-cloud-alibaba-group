###负载均衡Ribbon


    策略名	策略描述	实现说明
    RandomRule	随机选择一个 server	在 index 上随机，选择 index 对应位置的 Server
    RoundRobinRule	轮询选择 server	轮询 index，选择 index 对应位置的 server
    ZoneAvoidanceRule	复合判断 server 所在区域的性能和 server 的可用性选择 server	使用 ZoneAvoidancePredicate 和 AvailabilityPredicate 来判断是否选择某个 server。ZoneAvoidancePredicate 判断判定一个 zone 的运行性能是否可用，剔除不可用的 zone（的所有 server）；AvailabilityPredicate 用于过滤掉连接数过多的 server。
    BestAvailableRule	选择一个最小并发请求的 server	逐个考察 server，如果 server 被 tripped 了则忽略，在选择其中activeRequestsCount 最小的 server
    AvailabilityFilteringRule	过滤掉那些因为一直连接失败的被标记为 circuit tripped 的后端 server，并过滤掉那些高并发的的后端 server（activeConnections 超过配置的阈值）	使用一个 AvailabilityPredicate 来包含过滤 server 的逻辑，其实就就是检查 status 里记录的各个 server 的运行状态
    WeightedResponseTimeRule	根据 server 的响应时间分配一个 weight，响应时间越长，weight 越小，被选中的可能性越低	一个后台线程定期的从 status 里面读取评价响应时间，为每个 server 计算一个 weight。weight 的计算也比较简单，responseTime 减去每个 server 自己平均的 responseTime 是 server 的权重。当刚开始运行，没有形成 status 时，使用 RoundRobinRule 策略选择 server。
    RetryRule	对选定的负载均衡策略机上重试机制	在一个配置时间段内当选择 server 不成功，则一直尝试使用 subRule 的方式选择一个可用的 server

    默认情况下，Ribbon 采用 ZoneAvoidanceRule 规则。
    因为大多数公司是单机房，所以一般只有一个 zone，而 ZoneAvoidanceRule 在仅有一个 zone 的情况下，
    会退化成轮询的选择方式，所以会和 RoundRobinRule 规则类似。

###自定义Ribbon配置
   
    配置文件方式:只能针对单个服务进行配置相关负载均衡
    demo-provider:
      ribbon:
         NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule # 负载均衡规则全类名
    Spring JavaConfig 方式:可以配置单服务也可以配置默认
    创建 RibbonConfiguration、DefaultRibbonClientConfiguration、UserProviderRibbonClientConfiguration 三个配置类，
    实现 Ribbon 自定义配置
    注意一：Spring Boot 项目默认扫描 DemoConsumerApplication 所在包以及子包下的所有 Bean 们。而 @Configuration 注解也是一种 Bean，也会被扫描到。
          如果将 DefaultRibbonClientConfiguration 和 UserProviderRibbonClientConfiguration 放在 DemoConsumerApplication 所在包或子包中，
          将会被 Spring Boot 所扫描到，导致整个项目的 Ribbon 客户端都使用相同的 Ribbon 配置，就无法到达 Ribbon 客户端级别的自定义配置的目的
    注意二：为了避免多个 Ribbon 客户端级别的配置类创建的 Bean 之间互相冲突，Spring Cloud Netflix Ribbon 通过 SpringClientFactory 类，
          为每一个 Ribbon 客户端创建一个 Spring 子上下文。
          不过这里要注意，因为 DefaultRibbonClientConfiguration 和 UserProviderRibbonClientConfiguration 都创建了 IRule Bean，
          而 DefaultRibbonClientConfiguration 是在 Spring 父上下文生效，会和 UserProviderRibbonClientConfiguration 所在的 Spring 子上下文共享。
          这样就导致从 Spring 获取 IRule Bean 时，存在两个而不知道选择哪一个。
          因此，我们声明 UserProviderRibbonClientConfiguration 创建的 IRule Bean 为 @Primary，优先使用它。
