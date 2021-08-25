package com.springcloud.alibaba.demo.seata.storage.application.service;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.action.StorageAction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StorageTCCService {

    @Autowired
    private StorageAction storageAction;

    public ObjectResponse decreaseStorageTCC(CommodityDTO commodityDTO) {
        boolean decreaseStorage = storageAction.decreaseStorage(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (decreaseStorage){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }
}
