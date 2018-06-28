package com.wheel.rpc.container.handler;

import org.I0Itec.zkclient.ZkClient;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.wheel.rpc.core.model.ServiceGovernanceModel;

public class DegradationHandlerTest {
    
    @Test
    public void degradationFalse() {
        ZkClient zkClient = new ZkClient("192.168.56.101:2181");
        ServiceGovernanceModel serviceGovernanceModel = new ServiceGovernanceModel();
        serviceGovernanceModel.setDegradation(false);
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        String path = "/rpcwheel/com.wheel.rpc.core.test.IHello";
        zkClient.writeData(path, data);
    }
    
    @Test
    public void degradationTrue() {
        ZkClient zkClient = new ZkClient("192.168.56.101:2181");
        ServiceGovernanceModel serviceGovernanceModel = new ServiceGovernanceModel();
        serviceGovernanceModel.setDegradation(true);
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        String path = "/rpcwheel/com.wheel.rpc.core.test.IHello";
        zkClient.writeData(path, data);
    }
}
