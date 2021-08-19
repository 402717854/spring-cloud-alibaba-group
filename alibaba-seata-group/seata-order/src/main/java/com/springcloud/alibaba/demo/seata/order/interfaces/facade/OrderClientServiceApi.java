package com.springcloud.alibaba.demo.seata.order.interfaces.facade;

import com.springcloud.alibaba.demo.seata.common.client.OrderClientService;
import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.application.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderClientServiceApi implements OrderClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TOrderController.class);
    @Autowired
    private OrderService orderService;

    @Override
    public ObjectResponse<OrderDTO> createOrder(OrderDTO orderDTO) {
        LOGGER.info("远程服务请求订单微服务：{}",orderDTO.toString());
        return orderService.createOrder(orderDTO);
    }
}
