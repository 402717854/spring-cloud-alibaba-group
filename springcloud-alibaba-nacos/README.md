# spring-cloud-alibaba-group
###nacos官方地址
    https://nacos.io/zh-cn/

###验证nacos下线功能是否正常
    创建springcloud-alibab-nacos-provider和springcloud-alibab-nacos-provider2两个项目
    配置文件中设置相同的spring.application.name=nacos-provider
    在nacos服务列表界面进行下线功能验证
    
    能够实现下线功能的正常使用(只是在服务列表中剔除该服务地址并不会真正让服务下线)

###依赖版本的管理
    引入 Spring Boot、Spring Cloud、Spring Cloud Alibaba 三者 BOM 文件，进行依赖版本的管理，防止不兼容。
    在 https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E 文章中，
    Spring Cloud Alibaba 开发团队推荐了三者的依赖关系