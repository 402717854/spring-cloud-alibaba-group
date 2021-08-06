package com.springcloud.alibaba.demo.circuit;

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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExceptionRatioCircuitBreakerDemo {
    private static final String KEY = "some_service";

    public static void main(String[] args) throws Exception {
        initDegradeRule();
        registerStateChangeObserver();

        final int concurrency = 30;
        for (int i = 0; i < concurrency; i++) {
            sleep(1000);
            System.out.println("第"+i+"个请求");
            Entry entry = null;
            try {
                entry = SphU.entry(KEY);
                // Error probability is 45%
                if(i<10){
                    throw new RuntimeException("第"+i+"个请求抛出异常");
                }
            } catch (BlockException e) {
                System.out.println("第"+i+"个请求进入熔断");
            } catch (Throwable t) {
                System.out.println(t.getMessage());
                // It's required to record exception here manually.
                Tracer.traceEntry(t, entry);
            } finally {
                if (entry != null) {
                    entry.exit();
                }
            }
        }
    }
    //熔断器事件监听
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

    /**
     * 熔断降级规则
     */
    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule(KEY)
                //熔断策略，异常比例
                .setGrade(CircuitBreakerStrategy.ERROR_RATIO.getType())
                // Set ratio threshold to 50%.
                //慢调用比例模式下为异常比例模式下为对应的阈值
                .setCount(0.5d)
                //统计时长
                .setStatIntervalMs(15000)
                //熔断触发的最小请求数，请求数小于该值时即使异常比率超出阈值也不会熔断
                .setMinRequestAmount(10)
                // Retry timeout (in second)
                //熔断时长
                .setTimeWindow(10);
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
