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