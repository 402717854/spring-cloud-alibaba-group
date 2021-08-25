package com.springcloud.alibaba.demo.seata.storage.domain.storage.service.impl;

import com.springcloud.alibaba.demo.seata.common.dto.CommodityDTO;
import com.springcloud.alibaba.demo.seata.common.enums.RspStatusEnum;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.repository.TStorageMapper;
import com.springcloud.alibaba.demo.seata.storage.domain.storage.service.ITStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  库存服务实现类
 * </p>
 *
 * @author wys
 * @since 2021/08/19
 */
@Service
public class TStorageServiceImpl implements ITStorageService {

    @Resource
    private TStorageMapper tStorageMapper;

    @Override
    public ObjectResponse decreaseStorage(CommodityDTO commodityDTO) {
        int storage = tStorageMapper.decreaseStorage(commodityDTO.getCommodityCode(), commodityDTO.getCount());
        ObjectResponse<Object> response = new ObjectResponse<>();
        if (storage > 0){
            response.setStatus(RspStatusEnum.SUCCESS.getCode());
            response.setMessage(RspStatusEnum.SUCCESS.getMessage());
            return response;
        }
        response.setStatus(RspStatusEnum.FAIL.getCode());
        response.setMessage(RspStatusEnum.FAIL.getMessage());
        return response;
    }
}
