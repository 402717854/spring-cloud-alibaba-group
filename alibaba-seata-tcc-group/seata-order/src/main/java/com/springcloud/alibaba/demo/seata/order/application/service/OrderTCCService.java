package com.springcloud.alibaba.demo.seata.order.application.service;

import com.springcloud.alibaba.demo.seata.common.client.AccountClientService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.domain.order.action.OrderAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderTCCService {
    @Autowired
    private OrderAction orderAction;
    @Autowired
    private AccountClientService accountClientService;

    /**
     * 创建订单
     */
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO){
        ObjectResponse<OrderDTO> response = new ObjectResponse<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ObjectResponse objectResponse = accountClientService.decreaseAccountTcc(accountDTO);
        if (objectResponse.getStatus() != 200) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        orderDTO.setOrderNo(orderNo);
        orderAction.createOrder(orderDTO,orderNo);
        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    };
}
