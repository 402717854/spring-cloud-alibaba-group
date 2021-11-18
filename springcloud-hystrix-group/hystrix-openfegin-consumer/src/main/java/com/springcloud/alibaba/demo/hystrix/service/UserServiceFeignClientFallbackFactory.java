package com.springcloud.alibaba.demo.hystrix.service;

import feign.hystrix.FallbackFactory;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 创建 UserServiceFeignClient 对应 Fallback 实现类的工厂类
 */
@Component
public class UserServiceFeignClientFallbackFactory implements FallbackFactory<UserServiceFeignClient> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public UserServiceFeignClient create(Throwable cause) {
        //创建 DemoProviderFeignClient 的实现类，这样每个实现方法，能够一一对应，进行 fallback 处理逻辑。
        return new UserServiceFeignClient() {

            @Override
            public String getUser(Integer id) {
                logger.info("[getUserFallback][id({}) exception({})]", id, ExceptionUtils.getRootCauseMessage(cause));
                return "mock:User:" + id;
            }

        };
    }

}
