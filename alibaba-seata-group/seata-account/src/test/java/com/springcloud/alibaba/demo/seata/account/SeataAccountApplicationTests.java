package com.springcloud.alibaba.demo.seata.account;

import com.alibaba.fastjson.JSON;
import com.springcloud.alibaba.demo.seata.account.application.service.AccountService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SeataAccountApplicationTests {

    @Autowired
    private AccountService accountService;

    @Test
    public void decreaseAccount() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setUserId("1");
        accountDTO.setAmount(new BigDecimal(1));
        ObjectResponse objectResponse = accountService.decreaseAccount(accountDTO);
        System.out.println(JSON.toJSON(objectResponse));
    };
    @Test
    public void testGlobalLock(){
        accountService.testGlobalLock();
    };

}
