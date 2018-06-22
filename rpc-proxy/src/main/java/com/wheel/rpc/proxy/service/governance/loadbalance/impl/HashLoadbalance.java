package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import com.wheel.rpc.core.model.RefreshCallMethod;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 
 * 负载均衡 - 一致性Hash算法
 * 支持权重设计,构造静态Hash环
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:37:57
 */
public class HashLoadbalance extends AbstractLoadbalance {
    
    public HashLoadbalance(String serviceNameArgs) {
        super(serviceNameArgs);
    }
    
    @Override
    public ServiceProviderNode next() {
        return null;
    }
    
    @Override
    public void refresh(RefreshCallMethod callMethod) {
    }
}
