package com.springcloud.alibaba.demo.seata.storage.domain.storage.service;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;

/**
 * 仓库服务
 *
 * @author wys
 * @since 2020/08/19
 */
public interface ITStorageService{

    /**
     * 扣减库存
     */
    ObjectResponse decreaseStorage(CommodityDTO commodityDTO);
}
