package com.springcloud.alibaba.demo.seata.common.client;


import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: wys
 * @Description  账户服务接口
 * @Date Created in 2021/8/18 16:37
 */
@FeignClient(name = "seata-account")
public interface AccountClientService {

    /**
     * 从账户扣钱
     */
    @PostMapping("/account/decrease")
    ObjectResponse decreaseAccount(@RequestBody AccountDTO accountDTO);
    @PostMapping("/account/decrease/tcc")
    ObjectResponse decreaseAccountTcc(@RequestBody AccountDTO accountDTO);
}
