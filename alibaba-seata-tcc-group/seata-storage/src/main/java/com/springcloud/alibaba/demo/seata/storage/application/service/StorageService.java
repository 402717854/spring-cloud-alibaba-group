package com.springcloud.alibaba.demo.seata.storage.application.service;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.service.ITStorageService;
import io.seata.core.context.RootContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StorageService {

    @Autowired
    private ITStorageService itStorageService;

    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        log.info("库存服务全局事务id ：" + RootContext.getXID());
        return itStorageService.decreaseStorage(commodityDTO);
    }
}
