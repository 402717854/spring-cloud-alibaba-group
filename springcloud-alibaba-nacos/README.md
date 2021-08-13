# spring-cloud-alibaba-group
###验证nacos下线功能是否正常
创建springcloud-alibab-nacos-provider和springcloud-alibab-nacos-provider2两个项目
配置文件中设置相同的spring.application.name=nacos-provider
在nacos服务列表界面进行下线功能验证

能够实现下线功能的正常使用(只是在服务列表中剔除该服务地址并不会真正让服务下线)