package com.wheel.rpc.container.handler;

import org.I0Itec.zkclient.ZkClient;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.wheel.rpc.core.model.ServiceGovernanceModel;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月26日 下午5:44:59
 */
public class LimiterHandlerTest {
    
    @Test
    public void addLimiter() {
        ZkClient zkClient = new ZkClient("192.168.56.101:2181");
        ServiceGovernanceModel serviceGovernanceModel = new ServiceGovernanceModel();
        serviceGovernanceModel.setDegradation(false);
        serviceGovernanceModel.addLimiter("sayHello", 100);
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        String path = "/rpcwheel/com.wheel.rpc.core.test.IHello";
        zkClient.writeData(path, data);
    }
    
    @Test
    public void removeLimiter() {
        ZkClient zkClient = new ZkClient("192.168.56.101:2181");
        ServiceGovernanceModel serviceGovernanceModel = new ServiceGovernanceModel();
        serviceGovernanceModel.setDegradation(false);
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        String path = "/rpcwheel/com.wheel.rpc.core.test.IHello";
        zkClient.writeData(path, data);
    }
    
}
