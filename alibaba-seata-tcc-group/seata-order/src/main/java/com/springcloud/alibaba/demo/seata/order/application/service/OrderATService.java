package com.springcloud.alibaba.demo.seata.order.application.service;

import com.springcloud.alibaba.demo.seata.common.client.AccountClientService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.domain.order.action.OrderAction;
import com.springcloud.alibaba.demo.seata.order.domain.order.service.ITOrderService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderATService {
    @Autowired
    private ITOrderService itOrderService;
    @Autowired
    private AccountClientService accountClientService;

    /**
     * 创建订单
     */
    @GlobalTransactional
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO){
        ObjectResponse<OrderDTO> response = new ObjectResponse<>();
        //扣减用户账户
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId(orderDTO.getUserId());
        accountDTO.setAmount(orderDTO.getOrderAmount());
        ObjectResponse objectResponse = accountClientService.decreaseAccount(accountDTO);
        if (objectResponse.getStatus() != 200) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }
        String orderNo = UUID.randomUUID().toString().replace("-", "");
        orderDTO.setOrderNo(orderNo);
        response = itOrderService.createOrder(response, orderDTO);
        return response;
    };
}
