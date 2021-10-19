###自定义负载均衡器VersionRoundRobbinRule
      基于服务版本号的负载均衡
      基本思路:
          服务消费端没有配置版本号的情况下执行轮询负载，
          在配置版本号的情况下进行匹配版本号一致的服务提供者端,没有匹配到报错,匹配到则按版本号一致的服务列表进行轮询负载
      服务版本号设置
      1、服务提供者端配置文件设置spring.cloud.nacos.discovery.metadata.version=2.0.0
      2、注册中心元数据配置
      
      #客户端级别的自定义配置
      nacos-provider:
        ribbon:
            NFLoadBalancerRuleClassName: com.springcloud.alibaba.demo.loadbalance.VersionRoundRobbinRule
            Version: 1.0.0 #客户端调用服务版本号设置


######验证问题:不停机热部署
       生产环境中，配置文件部署在配置中心，同一服务的多个实例共享配置文件
       在接口更改升级的情况下，也就是版本号升级的情况下，如何做到不停机热部署