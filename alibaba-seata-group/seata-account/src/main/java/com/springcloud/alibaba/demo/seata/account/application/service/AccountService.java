package com.springcloud.alibaba.demo.seata.account.application.service;

import com.springcloud.alibaba.demo.seata.account.domain.account.action.AccountAction;
import com.springcloud.alibaba.demo.seata.account.domain.account.service.ITAccountService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 账户服务
 */
@Component
public class AccountService {

    @Autowired
    private ITAccountService itAccountService;

    @Autowired
    private AccountAction accountAction;

    public ObjectResponse decreaseAccount(AccountDTO accountDTO){
        return itAccountService.decreaseAccount(accountDTO);
    };
    public ObjectResponse decreaseAccountTCC(AccountDTO accountDTO){
        ObjectResponse<Object> response = new ObjectResponse<>();
        boolean decreaseAccount = accountAction.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount());
        if (!decreaseAccount){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    };

    public void testGlobalLock(){
        itAccountService.testGlobalLock();
    };
}
