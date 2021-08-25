package com.springcloud.alibaba.demo.seata.order.domain.order.action.impl;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.order.domain.order.action.OrderAction;
import com.springcloud.alibaba.demo.seata.order.domain.order.service.ITOrderService;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderActionImpl implements OrderAction {

    @Autowired
    private ITOrderService orderService;

    @Override
    public boolean createOrder(OrderDTO orderDTO, String orderNo) {
        return orderService.createOrder(orderDTO);
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String orderNo = (String) businessActionContext.getActionContext("orderNo");
        log.info("订单服务本地事务提交,actionName:{},xid:{},branchId:{},业务orderNo:{}",actionName,xid,branchId,orderNo);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String orderNo = (String) businessActionContext.getActionContext("orderNo");
        log.info("订单服务本地事务回滚,actionName:{},xid:{},branchId:{},业务orderNo:{}",actionName,xid,branchId,orderNo);
        orderService.deleteOrder(orderNo);
        return true;
    }
}
