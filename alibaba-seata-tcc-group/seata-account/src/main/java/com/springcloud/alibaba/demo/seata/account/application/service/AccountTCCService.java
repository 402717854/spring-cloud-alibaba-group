package com.springcloud.alibaba.demo.seata.account.application.service;

import com.alibaba.fastjson.JSON;
import com.springcloud.alibaba.demo.seata.account.domain.account.action.AccountAction;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountTCCService {

    @Autowired
    private AccountAction accountAction;

    public ObjectResponse decreaseAccountTCC(AccountDTO accountDTO){
        log.info("TCC账户服务入参:{}", JSON.toJSONString(accountDTO));
        ObjectResponse<Object> response = new ObjectResponse<>();
        boolean decreaseAccount = accountAction.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount());
        if (decreaseAccount){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    };

}
