###单实例Hystrix Dashboard 监控
####客户端
    客户端添加监控依赖
    <!-- 实现对 Actuator 的自动化配置 -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-actuator</artifactId>
    </dependency>
    客户端application.yaml配置文件，配置暴露 hystrix.stream 端点
    management:
        endpoints:
            web:
             exposure:
               include: 'hystrix.stream' # 需要开放的端点。默认值只打开 health 和 info 两个端点。通过设置 * ，可以开放所有端点。
    访问 http://127.0.0.1:8080/demo/get_user?id=1 地址，执行 Hystrix Command 的调用，产生 Hystrix 监控数据。
    访问 http://127.0.0.1:8080/actuator/hystrix.stream 地址，获得 Hystrix 监控数据
#####Dashboard搭建
    详见源码
    访问 http://127.0.0.1:9090/hystrix/ 地址，进入 Hystrix Dashboard 首页
    输入 http://127.0.0.1:8080/actuator/hystrix.stream 地址，抓取项目的 Hystrix 监控数据
    hystrix:
      dashboard:
        proxy-stream-allow-list: "127.0.0.1"
