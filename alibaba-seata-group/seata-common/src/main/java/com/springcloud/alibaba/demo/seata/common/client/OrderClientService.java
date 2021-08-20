package com.springcloud.alibaba.demo.seata.common.client;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: wys
 * @Description  订单服务接口
 * @Date Created in 2021/08/19 16:28
 */
@FeignClient("seata-order")
public interface OrderClientService {

    /**
     * 创建订单
     */
    @PostMapping("/order/create")
    ObjectResponse<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO);
    @PostMapping("/order/tcc/create")
    ObjectResponse<OrderDTO> createOrderTCC(@RequestBody OrderDTO orderDTO);
}
