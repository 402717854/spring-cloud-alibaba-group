package com.springcloud.alibaba.demo.seata.storage.domain.storage.action;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface StorageAction {
    /**
     * 扣用户钱
     */
    @TwoPhaseBusinessAction(name = "storage-decrease",commitMethod = "commit",rollbackMethod = "rollback")
    boolean decreaseStorage(@BusinessActionContextParameter(paramName = "commodityCode")String commodityCode,@BusinessActionContextParameter(paramName = "count") Integer count);
    boolean commit(BusinessActionContext businessActionContext);
    boolean rollback(BusinessActionContext businessActionContext);
}
