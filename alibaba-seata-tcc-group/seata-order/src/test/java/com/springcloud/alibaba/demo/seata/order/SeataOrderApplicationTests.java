package com.springcloud.alibaba.demo.seata.order;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.order.application.service.OrderTCCService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SeataOrderApplicationTests {

    @Autowired
    private OrderTCCService orderTCCService;

    @Test
    public void testCreateOrder(){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNo(UUID.randomUUID().toString());
        orderDTO.setOrderAmount(new BigDecimal(1000));
        orderDTO.setOrderCount(10);
        orderDTO.setUserId("1");
        orderDTO.setCommodityCode("999888");
        orderTCCService.createOrder(orderDTO);
    }
}
