简单配置文件:
####Spring Cloud Gateway 配置项，对应 GatewayProperties 类
    gateway:
      # 路由配置项，对应 RouteDefinition 数组
      routes:
        - id: user-service-prod
          uri: http://www.iocoder.cn
          predicates:
            - Path=/**
            - Weight=user-service, 90
        - id: user-service-canary
          uri: https://www.oschina.net
          predicates:
            - Path=/**
            - Weight=user-service, 10

Gateway 的权重路由仅仅提供了基本的灰度发布，实际还是需要做一定的改造

Gateway需要解决灰度发布的实际需求:
同一服务不同版本的灰度发布--根据权重进行匹配调用服务

    1、nacos服务注册中心进行元数据配置
       "version": "v1"
    2、配置文件:

        # Spring Cloud Gateway 配置项，对应 GatewayProperties 类
        gateway:
        # 路由配置项，对应 RouteDefinition 数组
        routes:
        - id: ReactiveCompositeDiscoveryClient_nacos-provider # 路由的编号
        uri: grayLb://nacos-provider # 路由到的目标地址
        predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
        - Path=/nacos-provider/**
        - Weight=nacos-provider,90
        filters:
        - RewritePath=/nacos-provider/(?<remaining>.*), /${remaining} # 将 /nacos-provider 前缀剔除
        - AddRequestHeader=Version,v1
        - id: ReactiveCompositeDiscoveryClient_nacos-provider2 # 路由的编号
        uri: grayLb://nacos-provider # 路由到的目标地址
        predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
        - Path=/nacos-provider/**
        - Weight=nacos-provider,10
        filters:
        - RewritePath=/nacos-provider/(?<remaining>.*), /${remaining} # 将 /nacos-provider 前缀剔除
        - AddRequestHeader=Version,v2
    3、引入依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-loadbalancer</artifactId>
        </dependency>
    4、重写负载均衡器GrayRoundRobinLoadBalancer
       主题思路：获取注册中心的服务列表，进行遍历，拿到服务的元数据version版本号，跟配置文件中的版本号进行对比获取相应的服务
    5、扩展gateway的filter过滤器GrayReactiveLoadBalancerClientFilter
    
    主体思路：
      a、复用gateway本身自带的权重路由断言工厂WeightRoutePredicateFactory获取对应路由
      b、在路由配置中添加请求头的版本号信息  - AddRequestHeader=Version,v1
      c、在过滤器中将版本号传给负载均衡器，负载均衡器获取注册中心的服务列表，进行遍历，拿到服务的元数据version版本号，跟配置文件中的版本号进行对比获取相应的服务地址
      d、拿到服务地址进行调用