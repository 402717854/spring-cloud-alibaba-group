Gateway需要解决访问认证需求:
同一服务不同版本的灰度发布-指定特定值访问服务

            1、创建 AuthGatewayFilterFactory 类，创建认证 Filter 的工厂
               ① 在类上添加 @Component 注解，保证 Gateway 在加载所有 GatewayFilterFactory Bean 的时候，
                  能够加载到我们定义的 AuthGatewayFilterFactory。

                ② 继承了 AbstractGatewayFilterFactory 抽象类，并将泛型参数 <C> 设置为我们定义的 AuthGatewayFilterFactory.Config 配置类。
                   这样，Gateway 在解析配置时，会转换成 Config 对象。
                
                注意，在 AuthGatewayFilterFactory 构造方法中，也需要传递 Config 类给父构造方法，保证能够正确创建 Config 对象。
                
                在 Config 类中，我们定义了两个属性：
                
                tokenHeaderName 属性：认证 Token 的 Header 名字，默认值为 token。
                userIdHeaderName 属性：认证后的 UserId 的 Header 名字，默认为 user-id。
                ③ 在 #apply(Config config) 方法中，我们通过内部类定义了需要创建的 GatewayFilter。我们来解释下整个 Filter 的逻辑：
                
                <1> 处，定义了一个存储 token 和 userId 映射的 Map，毕竟咱仅仅是一个提供“伪劣”的认证功能的 Filter。
                <2> 处，从请求 Header 中获取 token，作为认证标识。
                <3> 处，如果没有 token，则不进行认证。因为可能是无需认证的 API 接口。
                <4> 处，“伪劣”的认证逻辑，哈哈哈~实际场景下，一般调用远程的认证服务。
                <5> 处，通过 token 获取不到 userId，说明认证不通过，直接返回 401 状态码 + 提示文案，并不继续 Gateway 的过滤链，最终不会转发请求给目标 URI。
                <6> 处，通过 token 获取到 userId，说明认证通过，将 userId 添加到请求 Header，从而实现将 userId 传递给目标 URI。
                        同时，继续 Gateway 的过滤链，执行后续的过滤器。
            2、配置文件:
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
                    - id: oschina # 路由的编号
                      uri: https://www.oschina.net # 路由的目标地址
                      predicates: # 断言，作为路由的匹配条件，对应 RouteDefinition 数组
                        - Path=/oschina
                      filters: # 过滤器，对请求进行拦截，实现自定义的功能，对应 FilterDefinition 数组
                        - StripPrefix=1
                  # 默认过滤器，对应 FilterDefinition 数组
                  default-filters:
                    - name: Auth
                      args:
                        token-header-name: access-token
                  # 与 Spring Cloud 注册中心的集成，对应 DiscoveryLocatorProperties 类
                  discovery:
                    locator:
                      enabled: true # 是否开启，默认为 false 关闭
                      url-expression: "'lb://' + serviceId" # 路由的目标地址的表达式，默认为 "'lb://' + serviceId"


          主体思路：
          a、通过请求路径中header添加access-token:yunai
          b、在AuthGatewayFilterFactory中匹配map中的值