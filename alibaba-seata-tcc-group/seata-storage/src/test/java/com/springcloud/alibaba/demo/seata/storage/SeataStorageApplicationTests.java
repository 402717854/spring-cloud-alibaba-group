package com.springcloud.alibaba.demo.seata.storage;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.storage.application.service.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SeataStorageApplicationTests {

    @Autowired
    private StorageService storageService;

    @Test
    public void decreaseStorage() {
        CommodityDTO commodityDTO=new CommodityDTO();
        commodityDTO.setCommodityCode("C201901140001");
        commodityDTO.setCount(1);
        storageService.decreaseStorage(commodityDTO);
    }
}
