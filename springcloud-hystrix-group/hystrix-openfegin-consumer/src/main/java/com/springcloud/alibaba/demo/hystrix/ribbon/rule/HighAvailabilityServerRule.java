package com.springcloud.alibaba.demo.hystrix.ribbon.rule;

import com.netflix.client.config.IClientConfig;

import com.netflix.loadbalancer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class HighAvailabilityServerRule extends RoundRobinRule {

    private AtomicInteger nextServerCyclicCounter;

    public static final int DEFAULT_TIMER_INTERVAL = 30 * 1000;

    private int serverWeightTaskTimerInterval = DEFAULT_TIMER_INTERVAL;

    private static final Logger logger = LoggerFactory.getLogger(HighAvailabilityServerRule.class);

    private volatile List<Server> availabilityServers = new ArrayList<Server>();


    private ScheduledExecutorService scheduledExecutorService=null;

    protected AtomicBoolean serverWeightAssignmentInProgress = new AtomicBoolean(false);

    String name = "unknown";

    public HighAvailabilityServerRule() {
        super();
    }

    public HighAvailabilityServerRule(ILoadBalancer lb) {
        super(lb);
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb) {
        super.setLoadBalancer(lb);
        if (lb instanceof BaseLoadBalancer) {
            name = ((BaseLoadBalancer) lb).getName();
        }
        nextServerCyclicCounter = new AtomicInteger(0);
        initialize(lb);
    }

    void initialize(ILoadBalancer lb) {
        if(scheduledExecutorService!=null){
            scheduledExecutorService.shutdown();
        }
        scheduledExecutorService = new ScheduledThreadPoolExecutor(20, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "highAvailabilityServerRule-thread-schedule");
                return t;
            }
        });
        scheduledExecutorService.scheduleWithFixedDelay(new DynamicServerRunnable(),
                0,
                serverWeightTaskTimerInterval,TimeUnit.MILLISECONDS);

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                logger.info("Stopping NFLoadBalancer-serverWeightTimer-"
                                + name);
                scheduledExecutorService.shutdown();
            }
        }));
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;
        int count = 0;
        while (server == null&& count++ < 10) {
            List<Server> currentAvailabilityServers = availabilityServers;
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> allServers = lb.getAllServers();

            int serverCount = allServers.size();

            if (serverCount == 0) {
                return null;
            }

            if (CollectionUtils.isEmpty(currentAvailabilityServers)) {
                server =  super.choose(getLoadBalancer(), key);
                if(server == null) {
                    return server;
                }
            } else {
                int nextServerIndex = incrementAndGetModulo(currentAvailabilityServers.size());
                server = currentAvailabilityServers.get(nextServerIndex);
            }

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Next.
            server = null;
        }
        if (count >= 10) {
            logger.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }

    class DynamicServerRunnable implements Runnable {
        @Override
        public void run() {
            logger.info("执行周期队列任务");
            AvailabilityServer serverWeight = new AvailabilityServer();
            try {
                serverWeight.getAvailabilityServers();
            } catch (Exception e) {
                logger.error("Error running DynamicServerWeightTask for {}", name, e);
            }
        }
    }

    class AvailabilityServer {

        public void getAvailabilityServers() {
            ILoadBalancer lb = getLoadBalancer();
            if (lb == null) {
                return;
            }

            if (!serverWeightAssignmentInProgress.compareAndSet(false,  true))  {
                return;
            }

            try {
                logger.info("AvailabilityServer adjusting job started");
                AbstractLoadBalancer nlb = (AbstractLoadBalancer) lb;

                List<Server> servers = new ArrayList<Server>();
                for (Server server : nlb.getAllServers()) {
                    PingUrl pingUrl = new PingUrl();
                    boolean alive = pingUrl.isAlive(server);
                    if(alive){
                        servers.add(server);
                    }
                }
                setAvailabilityServers(servers);
            } catch (Exception e) {
                logger.error("Error calculating AvailabilityServer", e);
            } finally {
                serverWeightAssignmentInProgress.set(false);
            }

        }
    }

    void setAvailabilityServers(List<Server> servers) {
        this.availabilityServers = servers;
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        super.initWithNiwsConfig(clientConfig);
    }

    private int incrementAndGetModulo(int modulo) {
        for (;;) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next))
            {
                return next;
            }
        }
    }
}
