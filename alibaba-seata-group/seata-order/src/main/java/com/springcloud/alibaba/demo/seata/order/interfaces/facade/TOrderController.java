package com.springcloud.alibaba.demo.seata.order.interfaces.facade;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.application.service.OrderService;
import com.springcloud.alibaba.demo.seata.order.domain.order.service.ITOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  订单服务
 * </p>
 *
 * @author wys
 * @since 2021/08/19
 */
@RestController
@RequestMapping("/order")
public class TOrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TOrderController.class);

    @Autowired
    private OrderService orderService;

    @PostMapping("/create_order")
    public ObjectResponse<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO){
        LOGGER.info("请求订单微服务：{}",orderDTO.toString());
        return orderService.createOrder(orderDTO);
    }
}

