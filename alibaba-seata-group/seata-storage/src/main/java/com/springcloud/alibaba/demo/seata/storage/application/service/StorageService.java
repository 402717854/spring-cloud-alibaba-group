package com.springcloud.alibaba.demo.seata.storage.application.service;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.action.StorageAction;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.service.ITStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageService {

    @Autowired
    private ITStorageService itStorageService;
    @Autowired
    private StorageAction storageAction;

    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        return itStorageService.decreaseStorage(commodityDTO);
    }
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
