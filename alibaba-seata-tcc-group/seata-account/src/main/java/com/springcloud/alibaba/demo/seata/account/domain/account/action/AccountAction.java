package com.springcloud.alibaba.demo.seata.account.domain.account.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

import java.math.BigDecimal;

@LocalTCC
public interface AccountAction {
    /**
     * 扣用户钱
     */
    @TwoPhaseBusinessAction(name = "account-decrease",commitMethod = "commit",rollbackMethod = "rollback")
    boolean decreaseAccount(@BusinessActionContextParameter(paramName = "userId")String userId,@BusinessActionContextParameter(paramName = "amount") BigDecimal amount);
    boolean commit(BusinessActionContext businessActionContext);
    boolean rollback(BusinessActionContext businessActionContext);
}
