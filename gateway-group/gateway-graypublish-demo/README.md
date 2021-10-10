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
同一服务不同版本的灰度发布-指定特定值访问服务

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
                        - HeaderUsername=jack,tom
                      filters:
                        - RewritePath=/nacos-provider/(?<remaining>.*), /${remaining} # 将 /nacos-provider 前缀剔除
                        - AddRequestHeader=Version,v1
                    - id: ReactiveCompositeDiscoveryClient_nacos-provider2 # 路由的编号
                      uri: grayLb://nacos-provider # 路由到的目标地址
                      predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
                        - Path=/nacos-provider/**
                      filters:
                        - RewritePath=/nacos-provider/(?<remaining>.*), /${remaining} # 将 /nacos-provider 前缀剔除
                        - AddRequestHeader=Version,v2
            3、扩展断言工厂类HeaderUsernameRoutePredicateFactory
                通过断言配置- HeaderUsername=jack,tom,匹配http://localhost:8888/nacos-provider/divide?a=1&b=2请求路径中header值中是否包含Username=jack|tom
            4、引入依赖
                <dependency>
                    <groupId>org.springframework.cloud</groupId>
                    <artifactId>spring-cloud-starter-loadbalancer</artifactId>
                </dependency>
            5、重写负载均衡器GrayRoundRobinLoadBalancer
               主题思路：获取注册中心的服务列表，进行遍历，拿到服务的元数据version版本号，跟配置文件中的版本号进行对比获取相应的服务
            6、扩展gateway的filter过滤器GrayReactiveLoadBalancerClientFilter


          主体思路：
          a、扩展断言工厂HeaderUsernameRoutePredicateFactory，通过请求路径中header获取对应路由
          b、在路由配置中添加请求头的版本号信息  - AddRequestHeader=Version,v1
          c、在过滤器中将版本号传给负载均衡器，负载均衡器获取注册中心的服务列表，进行遍历，拿到服务的元数据version版本号，跟配置文件中的版本号进行对比获取相应的服务地址
          d、拿到服务地址进行调用