package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 负载均衡 - 最小负载(优先级低)
 * 用于解决服务器之间能力不均的情况,权重算法支持不易,暂不支持权重
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:40:23
 */
public class LeastLoadLoadbalance extends AbstractLoadbalance {
    
    public LeastLoadLoadbalance(String serviceNameArgs) {
        super(serviceNameArgs);
    }
    
    @Override
    public ServiceProviderNode next() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public void refresh() {
    }

}
