package com.springcloud.alibaba.demo.seata.account.domain.account.action.impl;

import com.springcloud.alibaba.demo.seata.account.domain.account.action.AccountAction;
import com.springcloud.alibaba.demo.seata.account.domain.account.repository.TAccountMapper;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
@Slf4j
public class AccountActionImpl implements AccountAction {

    @Resource
    private TAccountMapper tAccountMapper;

    @Override
    public boolean decreaseAccount(String userId, BigDecimal amount) {
        int account = tAccountMapper.decreaseAccount(userId, amount.doubleValue());
        if(account<=0){
            return false;
        }
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String userId = (String) businessActionContext.getActionContext("userId");
        log.info("账户服务本地事务提交,actionName:{},xid:{},branchId:{},业务userId:{}",actionName,xid,branchId,userId);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String userId = (String) businessActionContext.getActionContext("userId");
        BigDecimal amount = (BigDecimal) businessActionContext.getActionContext("amount");
        log.info("账户服务本地事务回滚,actionName:{},xid:{},branchId:{},业务userId:{}",actionName,xid,branchId,userId);
        tAccountMapper.increaseAccount(userId,amount.doubleValue());
        return true;
    }
}
