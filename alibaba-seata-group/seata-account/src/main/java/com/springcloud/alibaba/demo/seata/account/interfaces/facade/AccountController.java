package com.springcloud.alibaba.demo.seata.account.interfaces.facade;

import com.springcloud.alibaba.demo.seata.account.application.service.AccountService;
import com.springcloud.alibaba.demo.seata.common.client.AccountClientService;
import com.springcloud.alibaba.demo.seata.common.dto.AccountDTO;
import com.springcloud.alibaba.demo.seata.common.response.ObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/testGlobalLock")
    public void testGlobalLock(){
        accountService.testGlobalLock();
    };
}
