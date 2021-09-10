package com.springcloud.alibaba.demo;

import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.Event;
import com.alibaba.nacos.api.naming.listener.EventListener;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.common.constant.HttpHeaderConsts;
import com.springcloud.alibaba.demo.base.Params;
import com.springcloud.alibaba.demo.naming.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class Subscribe_ITCase{

    private NamingService naming;
    private int port=8848;
    @Autowired
    protected RestTemplate restTemplate;

    private volatile List<Instance> instances = Collections.emptyList();

    public static final int TEST_PORT = 8080;

    @Before
    public void init() throws Exception {
        prepareServer(port);
        instances.clear();
        if (naming == null) {
            //TimeUnit.SECONDS.sleep(10);
            naming = NamingFactory.createNamingService("127.0.0.1" + ":" + port);
        }
        while (true) {
            if (!"UP".equals(naming.getServerStatus())) {
                Thread.sleep(1000L);
                continue;
            }
            break;
        }
    }

    /**
     * 添加IP，收到通知
     *
     * @throws Exception
     */
    @Test
    public void subscribeAdd() throws Exception {
        String serviceName = randomDomainName();

        naming.subscribe(serviceName, new EventListener() {
            @Override
            public void onEvent(Event event) {
                System.out.println("===========订阅已注册服务=============");
                System.out.println(((NamingEvent) event).getServiceName());
                System.out.println(((NamingEvent) event).getInstances());
                instances = ((NamingEvent) event).getInstances();
            }
        });

        naming.registerInstance(serviceName, "127.0.0.1", TEST_PORT, "c1");

        while (instances.isEmpty()) {
            Thread.sleep(1000L);
        }

        Assert.assertTrue(verifyInstanceList(instances, naming.getAllInstances(serviceName)));
    }

    public void prepareServer(int localPort) throws Exception{
        prepareServer(localPort, "UP");
    }

    public void prepareServer(int localPort, String status) throws Exception {
        String url = "http://127.0.0.1:" + localPort + "/nacos/v1/ns/operator/switches?entry=overriddenServerStatus&value=" + status;
        MultiValueMap<String, String> params = Params.newParams().done();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaderConsts.USER_AGENT_HEADER, "Nacos-Server");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);
        ResponseEntity<String> respose = restTemplate.exchange(
                builder.toUriString(), HttpMethod.GET, entity, String.class);

        System.out.println(respose);

        url = "http://127.0.0.1:" + localPort + "/nacos/v1/ns/operator/switches?entry=autoChangeHealthCheckEnabled&value=" + false;
        UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(url)
                .queryParams(params);
        respose = restTemplate.exchange(
                builder2.toUriString(), HttpMethod.GET, entity, String.class);
        System.out.println(respose);
    }
    public static String randomDomainName() {
        StringBuilder sb = new StringBuilder();
        sb.append("jinhan");
        for (int i = 0; i < 2; i++) {
            sb.append(RandomUtils.getStringWithNumAndCha(5));
            sb.append(".");
        }
        int i = RandomUtils.getIntegerBetween(0, 2);
        if (i == 0) {
            sb.append("com");
        } else {
            sb.append("net");
        }
        return sb.toString();
    }
    public static boolean verifyInstanceList(List<Instance> instanceList1, List<Instance> instanceList2) {
        Map<String, Instance> instanceMap = new HashMap<String, Instance>();
        for (Instance instance : instanceList1) {
            instanceMap.put(instance.getIp(), instance);
        }

        Map<String, Instance> instanceGetMap = new HashMap<String, Instance>();
        for (Instance instance : instanceList2) {
            instanceGetMap.put(instance.getIp(), instance);
        }

        for (String ip : instanceMap.keySet()) {
            if (!instanceGetMap.containsKey(ip)) {
                return false;
            }
            if (!verifyInstance(instanceMap.get(ip), instanceGetMap.get(ip))) {
                return false;
            }
        }
        return true;
    }
    public static boolean verifyInstance(Instance i1, Instance i2) {

        if (!i1.getIp().equals(i2.getIp()) || i1.getPort() != i2.getPort() ||
                i1.getWeight() != i2.getWeight() || i1.isHealthy() != i2.isHealthy() ||
                !i1.getMetadata().equals(i2.getMetadata())) {
            return false;
        }
        return true;

    }
}
