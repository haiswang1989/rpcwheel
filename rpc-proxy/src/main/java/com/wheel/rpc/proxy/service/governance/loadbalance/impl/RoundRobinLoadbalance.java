package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import com.wheel.rpc.core.model.RefreshCallMethod;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 负载均衡 - 轮询算法
 * 
 * 效果和随机算法类似,有状态,需要记着上次call的哪台机器,权重算法不太好设计(预先构造环,随机打乱)
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:36:10
 */
public class RoundRobinLoadbalance extends AbstractLoadbalance {
    
    public RoundRobinLoadbalance(String serviceNameArgs) {
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
