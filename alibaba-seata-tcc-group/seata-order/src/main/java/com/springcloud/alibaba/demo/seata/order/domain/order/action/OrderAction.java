package com.springcloud.alibaba.demo.seata.order.domain.order.action;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface OrderAction {
    @TwoPhaseBusinessAction(name = "order-create",commitMethod = "commit",rollbackMethod = "rollback")
    boolean createOrder(OrderDTO orderDTO,@BusinessActionContextParameter(paramName = "orderNo")String orderNo);
    boolean commit(BusinessActionContext businessActionContext);
    boolean rollback(BusinessActionContext businessActionContext);
}
