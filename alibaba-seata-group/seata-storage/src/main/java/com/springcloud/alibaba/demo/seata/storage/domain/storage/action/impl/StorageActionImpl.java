package com.springcloud.alibaba.demo.seata.storage.domain.storage.action.impl;

import com.springcloud.alibaba.demo.seata.storage.domain.storage.action.StorageAction;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.repository.TStorageMapper;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
@Slf4j
public class StorageActionImpl implements StorageAction {

    @Resource
    private TStorageMapper tStorageMapper;

    @Override
    public boolean decreaseStorage(String commodityCode, Integer count) {
        int i = tStorageMapper.decreaseStorage(commodityCode, count);
        if(i<=0){
            return false;
        }
        return true;
    }

    @Override
    public boolean commit(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String commodityCode = (String) businessActionContext.getActionContext("commodityCode");
        log.info("库存服务本地事务提交,actionName:{},xid:{},branchId:{},业务commodityCode:{}",actionName,xid,branchId,commodityCode);
        return true;
    }

    @Override
    public boolean rollback(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        long branchId = businessActionContext.getBranchId();
        String actionName = businessActionContext.getActionName();
        String commodityCode = (String) businessActionContext.getActionContext("commodityCode");
        Integer count = (Integer) businessActionContext.getActionContext("count");
        log.info("库存服务本地事务回滚,actionName:{},xid:{},branchId:{},业务commodityCode:{}",actionName,xid,branchId,commodityCode);
        tStorageMapper.increaseStorage(commodityCode,count);
        return true;
    }
}
