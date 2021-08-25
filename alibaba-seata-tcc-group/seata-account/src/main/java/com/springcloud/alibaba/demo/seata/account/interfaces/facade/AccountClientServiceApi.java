package com.springcloud.alibaba.demo.seata.account.interfaces.facade;

import com.springcloud.alibaba.demo.seata.account.application.service.AccountTCCService;
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
    private AccountTCCService accountTCCService;

    @Override
    public ObjectResponse decreaseAccountTcc(AccountDTO accountDTO) {
        log.info("TCC远程服务请求账户微服务：{}",accountDTO.toString());
        return accountTCCService.decreaseAccountTCC(accountDTO);
    }
}
