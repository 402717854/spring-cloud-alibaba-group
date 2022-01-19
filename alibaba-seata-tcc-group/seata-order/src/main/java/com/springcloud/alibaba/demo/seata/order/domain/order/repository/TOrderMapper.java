package com.springcloud.alibaba.demo.seata.order.domain.order.repository;

import com.springcloud.alibaba.demo.seata.order.domain.order.entity.TOrder;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
public interface TOrderMapper{

    /**
     * 创建订单
     * @Param:  order 订单信息
     * @Return:
     */
    void createOrder(@Param("order") TOrder order);

    /**
     * 删除订单
     * @param orderNo
     */
    void deleteOrder(@Param("orderNo") String orderNo);
}
