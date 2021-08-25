package com.springcloud.alibaba.demo.seata.order.interfaces.facade;

import com.springcloud.alibaba.demo.seata.common.client.OrderClientService;
import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.application.service.OrderATService;
import com.springcloud.alibaba.demo.seata.order.application.service.OrderTCCService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderClientServiceApi implements OrderClientService {

    @Autowired
    private OrderTCCService orderTCCService;
    @Autowired
    private OrderATService orderATService;

    @Override
    public ObjectResponse<OrderDTO> createOrderTCC(OrderDTO orderDTO) {
        log.info("TCC远程服务请求订单微服务：{}",orderDTO.toString());
        return orderTCCService.createOrder(orderDTO);
    }

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        log.info("AT远程服务请求订单微服务：{}",orderDTO.toString());
        return orderATService.createOrder(orderDTO);
    }
}
