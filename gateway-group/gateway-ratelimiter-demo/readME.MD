###GATEWAY请求限流
   
       cloud:
        # Spring Cloud Gateway 配置项，对应 GatewayProperties 类
        gateway:
          # 路由配置项，对应 RouteDefinition 数组
          routes:
            - id: yudaoyuanma # 路由的编号
              uri: http://www.iocoder.cn # 路由到的目标地址
              predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
                - Path=/blog
              filters:
                - StripPrefix=1
                - name: RequestRateLimiter
                  args:
                    redis-rate-limiter.replenishRate: 1 # 令牌桶的每秒放的数量
                    redis-rate-limiter.burstCapacity: 2 # 令牌桶的最大令牌数
                    key-resolver: "#{@ipKeyResolver}" # 获取限流 KEY 的 Bean 的名字
            - id: oschina # 路由的编号
              uri: https://www.oschina.net # 路由的目标地址
              predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
                - Path=/oschina
              filters: # 过滤器，对请求进行拦截，实现自定义的功能，对应 FilterDefinition 数组
                - StripPrefix=1
        
        ① 针对编号为 yudaoyuanma 的路由，我们在 filter 配置项，添加了限流过滤器 RequestRateLimiter，其配置参数如下：

            redis-rate-limiter.replenishRate：令牌桶的每秒放的数量。
            redis-rate-limiter.burstCapacity：令牌桶的最大令牌数。
            
            burstCapacity 参数，我们可以近似理解为是每秒最大的请求数。因此每请求一次，都会从桶里获取掉一块令牌。
            replenishRate 参数，我们可以近似理解为是每秒平均的请求数。假设在令牌桶为空的情况下，一秒最多放这么多令牌，所以最大请求书当然也是这么多。
            
            实际上，在令牌桶满的情况下，每秒最大的请求数是 burstCapacity + replenishRate，好好理解下。
            
            key-resolver：获取限流 KEY 的 Bean 的名字。


         GATEWAY请求限流源码相关类RequestRateLimiterGatewayFilterFactory