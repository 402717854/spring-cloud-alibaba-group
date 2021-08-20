package com.springcloud.alibaba.demo.seata.order.domain.order.service;

import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;

/**
 * <p>
 *  创建订单
 * </p>
 *
 * @author wys
 * @since 2021/08/19
 */
public interface ITOrderService{

    /**
     * 创建订单
     */
    ObjectResponse<OrderDTO> createOrder(ObjectResponse<OrderDTO> response,OrderDTO orderDTO);
    /**
     * 创建订单
     */
    Boolean createOrder(OrderDTO orderDTO);

    /**
     * 删除订单
     * @param orderNo
     * @return
     */
    void deleteOrder(String orderNo);
}
