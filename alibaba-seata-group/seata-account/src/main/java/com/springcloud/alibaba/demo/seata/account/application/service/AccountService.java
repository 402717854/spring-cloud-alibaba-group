package com.springcloud.alibaba.demo.seata.account.application.service;

import com.springcloud.alibaba.demo.seata.account.domain.account.service.ITAccountService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountService {

    @Autowired
    private ITAccountService itAccountService;

    public ObjectResponse decreaseAccount(AccountDTO accountDTO){
        return itAccountService.decreaseAccount(accountDTO);
    };

    public void testGlobalLock(){
        itAccountService.testGlobalLock();
    };
}
