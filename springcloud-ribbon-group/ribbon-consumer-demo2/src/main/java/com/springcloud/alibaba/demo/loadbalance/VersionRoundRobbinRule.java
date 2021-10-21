package com.springcloud.alibaba.demo.loadbalance;

import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.netflix.client.config.CommonClientConfigKey;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class VersionRoundRobbinRule extends AbstractLoadBalancerRule {

    private AtomicInteger nextServerCyclicCounter;
    private static final String SERVER_VERSION_DEFAULT_VALUE = "0";

    private String SERVER_VERSION_VALUE;


    private static Logger log = LoggerFactory.getLogger(RoundRobinRule.class);

    public VersionRoundRobbinRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    public VersionRoundRobbinRule(ILoadBalancer lb) {
        this();
        setLoadBalancer(lb);
    }
    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        SERVER_VERSION_VALUE = clientConfig.get(CommonClientConfigKey.Version,SERVER_VERSION_DEFAULT_VALUE);
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }

        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }
            //转换为NacosServer列表
            List<NacosServer> nacosServers=new ArrayList<>();
            for (int i = 0; i < allServers.size(); i++) {
                NacosServer nacosServer = (NacosServer)allServers.get(i);
                nacosServers.add(nacosServer);
            }
            if("0".equals(SERVER_VERSION_VALUE)){
                int nextServerIndex = incrementAndGetModulo(serverCount);

                server = nacosServers.get(nextServerIndex);
            }else{
                //通过版本号进行负载均衡
                //nacos注册中心服务列表没有配置版本号或者一部分配置版本号，所有服务进行轮询
                //配置版本号
                List<NacosServer> equalVersionnacosServers = nacosServers.stream().filter(nacosServer -> {
                    String version = nacosServer.getMetadata().get("version");
                    if (SERVER_VERSION_VALUE.equals(version)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

                if(CollectionUtils.isEmpty(equalVersionnacosServers)){
                    log.warn("No equal version up servers available from load balancer: " + lb);
                    return null;
                }
                int nextServerIndex = incrementAndGetModulo(equalVersionnacosServers.size());

                server = equalVersionnacosServers.get(nextServerIndex);
            }


            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }

            // Next.
            server = null;
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }
    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
                return next;
        }
    }
}
