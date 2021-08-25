package com.springcloud.alibaba.demo.seata.account.domain.account.repository;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author wys
 * @since 2021/08/18
 */
public interface TAccountMapper{

    int decreaseAccount(@Param("userId") String userId, @Param("amount") Double amount);

    int increaseAccount(@Param("userId") String userId, @Param("amount") Double amount);


    int testGlobalLock(@Param("userId") String userId);
}
