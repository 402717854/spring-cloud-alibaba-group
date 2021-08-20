package com.springcloud.alibaba.demo.seata.order.domain.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.springcloud.alibaba.demo.seata.common.dto.OrderDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.order.domain.order.entity.TOrder;
import com.springcloud.alibaba.demo.seata.order.domain.order.repository.TOrderMapper;
import com.springcloud.alibaba.demo.seata.order.domain.order.service.ITOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author heshouyou
 * @since 2019-01-13
 */
@Service
@Slf4j
public class TOrderServiceImpl implements ITOrderService {

    @Resource
    private TOrderMapper orderMapper;

    /**
     * 创建订单
     * @Param:  OrderDTO  订单对象
     * @Return:  OrderDTO  订单对象
     */
    @Override
    public ObjectResponse<OrderDTO> createOrder(ObjectResponse<OrderDTO> response,OrderDTO orderDTO) {
        //生成订单号
        orderDTO.setOrderNo(UUID.randomUUID().toString().replace("-",""));
        //生成订单
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(orderDTO,tOrder);
        tOrder.setCount(orderDTO.getOrderCount());
        tOrder.setAmount(orderDTO.getOrderAmount().doubleValue());
        try {
            orderMapper.createOrder(tOrder);
        } catch (Exception e) {
            response.setStatus(RspStatusEnum.FAIL.getCode());
            response.setMessage(RspStatusEnum.FAIL.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.SUCCESS.getCode());
        response.setMessage(RspStatusEnum.SUCCESS.getMessage());
        return response;
    }

    @Override
    public Boolean createOrder(OrderDTO orderDTO) {
        try{
            //生成订单
            TOrder tOrder = new TOrder();
            BeanUtils.copyProperties(orderDTO,tOrder);
            tOrder.setCount(orderDTO.getOrderCount());
            tOrder.setAmount(orderDTO.getOrderAmount().doubleValue());
            orderMapper.createOrder(tOrder);
        }catch (Exception e){
            log.error("创建订单失败,orderDTO:{},error:{}", JSON.toJSONString(orderDTO),e);
            return false;
        }
        return true;
    }

    @Override
    public void deleteOrder(String orderNo) {
        orderMapper.deleteOrder(orderNo);
    }
}
