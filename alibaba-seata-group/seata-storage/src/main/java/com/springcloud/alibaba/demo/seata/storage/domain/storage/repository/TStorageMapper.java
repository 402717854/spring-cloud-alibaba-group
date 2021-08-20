package com.springcloud.alibaba.demo.seata.storage.domain.storage.repository;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wys
 * @since 2020/08/19
 */
public interface TStorageMapper{

    /**
     * 扣减商品库存
     * @Param: commodityCode 商品code  count扣减数量
     * @Return:
     */
    int decreaseStorage(@Param("commodityCode") String commodityCode, @Param("count") Integer count);
    int increaseStorage(@Param("commodityCode") String commodityCode, @Param("count") Integer count);
}
