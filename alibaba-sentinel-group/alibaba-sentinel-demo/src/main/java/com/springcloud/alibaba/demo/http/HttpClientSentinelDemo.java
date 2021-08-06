package com.springcloud.alibaba.demo.http;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreaker;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.CircuitBreakerStrategy;
import com.alibaba.csp.sentinel.slots.block.degrade.circuitbreaker.EventObserverRegistry;
import com.alibaba.csp.sentinel.util.TimeUtil;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class HttpClientSentinelDemo {

    private static final String KEY = "some_method";

    public static void main(String[] args) {
        initDegradeRule();
        registerStateChangeObserver();

        int concurrency = 30;
        for (int i = 0; i < concurrency; i++) {
            sleep(1000);
            System.out.println("第"+i+"个请求");
            Entry entry = null;
            try {
                entry = SphU.entry(KEY);
                RestTemplate restTemplate = new RestTemplate();
                Map<String, Object> map = new HashMap<>();
                String url = "http://localhost:8081/httpclient/back/"+i;
                URI uri = URI.create(url);
                map.put("id",11);
                String resultStr = restTemplate.getForObject(url, String.class, map);
                System.out.println(resultStr);
            } catch (BlockException e) {
                System.out.println("第"+i+"个请求进入熔断");
            }catch (Throwable t) {
                Tracer.traceEntry(t, entry);
                System.out.println(t.getMessage());
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }
    private static void registerStateChangeObserver() {
        EventObserverRegistry.getInstance().addStateChangeObserver("logging",
                (prevState, newState, rule, snapshotValue) -> {
                    if (newState == CircuitBreaker.State.OPEN) {
                        System.err.println(String.format("%s -> OPEN at %d, snapshotValue=%.2f", prevState.name(),
                                TimeUtil.currentTimeMillis(), snapshotValue));
                    } else {
                        System.err.println(String.format("%s -> %s at %d", prevState.name(), newState.name(),
                                TimeUtil.currentTimeMillis()));
                    }
                });
    }

    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(KEY)
                //熔断策略，支持慢调用比例策略
                .setGrade(CircuitBreakerStrategy.SLOW_REQUEST_RATIO.getType())
                // Max allowed response time
                //慢调用比例模式下为慢调用临界 RT（超出该值计为慢调用）
                .setCount(50)
                // Retry timeout (in second)
                //熔断时长
                .setTimeWindow(10)
                // Circuit breaker opens when slow request ratio > 60%
                //慢调用比例阈值，仅慢调用比例模式有效
                .setSlowRatioThreshold(0.7)
                //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
                .setMinRequestAmount(5)
                //统计时长
                .setStatIntervalMs(10000);
        rules.add(rule);

        DegradeRuleManager.loadRules(rules);
        System.out.println("Degrade rule loaded: " + rules);
    }
    private static void sleep(int timeMs) {
        try {
            TimeUnit.MILLISECONDS.sleep(timeMs);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
