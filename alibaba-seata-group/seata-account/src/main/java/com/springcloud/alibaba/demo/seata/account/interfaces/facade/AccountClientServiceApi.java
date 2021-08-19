package com.springcloud.alibaba.demo.seata.account.interfaces.facade;

import com.springcloud.alibaba.demo.seata.account.application.service.AccountService;
import com.springcloud.alibaba.demo.seata.common.client.AccountClientService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class AccountClientServiceApi implements AccountClientService {

    @Autowired
    private AccountService accountService;
    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        log.info("远程服务请求账户微服务：{}",accountDTO.toString());
        return accountService.decreaseAccount(accountDTO);
    }
}
