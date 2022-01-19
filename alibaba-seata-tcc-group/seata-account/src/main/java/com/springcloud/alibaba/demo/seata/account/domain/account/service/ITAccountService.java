package com.springcloud.alibaba.demo.seata.account.domain.account.service;


import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wys
 * @since 2020/08/18
 */
public interface ITAccountService{

    /**
     * 扣用户钱
     */
    ObjectResponse decreaseAccount(AccountDTO accountDTO);

    void testGlobalLock();
}
