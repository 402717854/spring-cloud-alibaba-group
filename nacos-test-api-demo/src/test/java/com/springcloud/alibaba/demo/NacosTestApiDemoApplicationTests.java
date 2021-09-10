package com.springcloud.alibaba.demo;

import com.alibaba.fastjson.JSON;
import com.springcloud.alibaba.demo.base.HttpClient4Test;
import com.springcloud.alibaba.demo.base.Params;
import com.springcloud.alibaba.demo.naming.NamingBase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@SpringBootTest
@RunWith(SpringRunner.class)
public class NacosTestApiDemoApplicationTests extends HttpClient4Test {

    private int port=8848;

    @Before
    public void setUp() throws Exception {
        String url = String.format("http://localhost:%d/", port);
        this.base = new URL(url);
        //prepareData();
    }

    @Test
    public void metrics() throws Exception {
        ResponseEntity<String> response = request("/nacos/v1/ns/operator/metrics",
                Params.newParams()
                        .done(),
                String.class);
        System.out.println(response.getBody());
    }
    /**
     * @TCDescription : 根据serviceName创建服务
     * @TestStep :
     * @ExpectResult :
     */
    @Test
    public void createService() throws Exception {
        String serviceName = NamingBase.randomDomainName();
        ResponseEntity<String> response = request(NamingBase.NAMING_CONTROLLER_PATH + "/service",
                Params.newParams()
                        .appendParam("serviceName", serviceName)
                        .appendParam("protectThreshold", "0.3")
                        .done(),
                String.class,
                HttpMethod.POST);
        System.out.println(JSON.toJSON(response));

        namingServiceDelete(serviceName);
    }
    /**
     * @TCDescription : 根据serviceName获取服务信息
     * @TestStep :
     * @ExpectResult :
     */
    @Test
    public void getService() throws Exception {
        String serviceName = NamingBase.randomDomainName();
        ResponseEntity<String> response = request(NamingBase.NAMING_CONTROLLER_PATH + "/service",
                Params.newParams()
                        .appendParam("serviceName", serviceName)
                        .appendParam("protectThreshold", "0.3")
                        .done(),
                String.class,
                HttpMethod.POST);
        System.out.println(response.getBody());
        //get service
        response = request(NamingBase.NAMING_CONTROLLER_PATH + "/service",
                Params.newParams()
                        .appendParam("serviceName", serviceName)
                        .appendParam("protectThreshold", "0.3")
                        .done(),
                String.class);

        System.out.println(response.getBody());

        namingServiceDelete(serviceName);
    }
    private void namingServiceDelete(String serviceName) {
        //delete service
        ResponseEntity<String> response = request(NamingBase.NAMING_CONTROLLER_PATH + "/service",
                Params.newParams()
                        .appendParam("serviceName", serviceName)
                        .appendParam("protectThreshold", "0.3")
                        .done(),
                String.class,
                HttpMethod.DELETE);
    }
}
