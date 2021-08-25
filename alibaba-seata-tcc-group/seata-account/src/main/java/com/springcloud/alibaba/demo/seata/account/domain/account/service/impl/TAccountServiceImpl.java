package com.springcloud.alibaba.demo.seata.account.domain.account.service.impl;

import com.springcloud.alibaba.demo.seata.account.domain.account.repository.TAccountMapper;
import com.springcloud.alibaba.demo.seata.account.domain.account.service.ITAccountService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wys
 * @since 2020/08/18
 */
@Service
public class TAccountServiceImpl implements ITAccountService {

    @Resource
    private TAccountMapper tAccountMapper;

    @Override
    public ObjectResponse decreaseAccount(AccountDTO accountDTO) {
        int account = tAccountMapper.decreaseAccount(accountDTO.getUserId(), accountDTO.getAmount().doubleValue());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (account > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void testGlobalLock() {
        tAccountMapper.testGlobalLock("1");
        System.out.println("Hi, i got lock, i will do some thing with holding this lock.");
    }
}
