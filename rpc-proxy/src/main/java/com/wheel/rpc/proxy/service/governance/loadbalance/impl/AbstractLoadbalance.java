package com.wheel.rpc.proxy.service.governance.loadbalance.impl;


import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.service.governance.ILoadbalance;

/**
 * 默认的负载均衡的实现
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:31:22
 */
public abstract class AbstractLoadbalance implements ILoadbalance {
    
    /** 服务的名称 */
    protected String serviceName;
    
    public AbstractLoadbalance(String serviceNameArgs) {
        this.serviceName = serviceNameArgs;
    }
    
    @Override
    public ServiceProviderNode next() {
        return null;
    }
    
    @Override
    public void refresh(ServiceGovernanceModel oldServiceGovernanceModel,
            ServiceGovernanceModel newServiceGovernanceModel) {
        
    }
}
