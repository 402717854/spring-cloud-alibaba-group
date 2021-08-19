package com.springcloud.alibaba.demo.seata.storage.application.service;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.service.ITStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageService {

    @Autowired
    private ITStorageService itStorageService;

    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        return itStorageService.decreaseStorage(commodityDTO);
    }
}
