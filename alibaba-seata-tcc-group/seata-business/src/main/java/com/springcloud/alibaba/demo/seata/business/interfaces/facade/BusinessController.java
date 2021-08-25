package com.springcloud.alibaba.demo.seata.business.interfaces.facade;

import com.springcloud.alibaba.demo.seata.business.application.service.BusinessTccService;
import com.springcloud.alibaba.demo.seata.common.dto.BusinessDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: wys
 * @Description  Dubbo业务执行入口
 * @Date Created in 2021/08/19
 */
@RestController
@RequestMapping("/business")
@Slf4j
public class BusinessController {

    @Autowired
    private BusinessTccService businessTccService;

    /**
     * 模拟用户购买商品下单业务逻辑流程
     * @Param:
     * @Return:
     */
    @PostMapping("/tcc/buy")
    public ObjectResponse handleBusiness(@RequestBody BusinessDTO businessDTO){
        log.info("请求参数：{}",businessDTO.toString());
        return businessTccService.handleBusiness(businessDTO);
    }
}
