package com.springcloud.alibaba.demo.seata.account.application.service;

import com.alibaba.fastjson.JSON;
import com.springcloud.alibaba.demo.seata.account.domain.account.action.AccountAction;
import com.springcloud.alibaba.demo.seata.account.domain.account.service.ITAccountService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountATService {

    @Autowired
    private ITAccountService itAccountService;

    public ObjectResponse decreaseAccount(AccountDTO accountDTO){
        log.info("AT账户服务入参:{}", JSON.toJSONString(accountDTO));
        ObjectResponse<Object> response= itAccountService.decreaseAccount(accountDTO);
        return response;
    };

}
