package com.springcloud.alibaba.demo.seata.storage.interfaces.facade;

import com.springcloud.alibaba.demo.seata.common.client.StorageClientService;
import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.application.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class StorageClientServiceApi implements StorageClientService {

    @Autowired
    private StorageService storageService;
    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        log.info("远程服务请求库存微服务：{}",commodityDTO.toString());
        return storageService.decreaseStorage(commodityDTO);
    }

    @Override
    public ObjectResponse decreaseStorageTCC(CommodityDTO commodityDTO) {
        log.info("远程服务请求库存微服务：{}",commodityDTO.toString());
        return storageService.decreaseStorageTCC(commodityDTO);
    }
}
