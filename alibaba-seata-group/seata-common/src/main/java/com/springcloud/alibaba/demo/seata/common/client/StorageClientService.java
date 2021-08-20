package com.springcloud.alibaba.demo.seata.common.client;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: wys
 * @Description  订单服务接口
 * @Date Created in 2021/08/19 16:28
 */
@FeignClient("seata-storage")
public interface StorageClientService {

    /**
     * 扣减库存
     */
    @PostMapping("/storage/decrease")
    public ObjectResponse decreaseStorage(@RequestBody CommodityDTO commodityDTO);
    @PostMapping("/storage/decrease/tcc")
    public ObjectResponse decreaseStorageTCC(@RequestBody CommodityDTO commodityDTO);
}
