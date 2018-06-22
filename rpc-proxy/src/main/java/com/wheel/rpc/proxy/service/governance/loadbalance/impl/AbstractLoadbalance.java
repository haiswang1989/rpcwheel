package com.wheel.rpc.proxy.service.governance.loadbalance.impl;


import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.LoadBalance;
import com.wheel.rpc.core.model.RefreshCallMethod;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.proxy.service.governance.loadbalance.ILoadbalance;

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
    
    /**
     * 负载均衡的策略是否发生变化
     * @param localLoadBalanceStrategy
     * @return
     */
    protected boolean isLoadbanlanceStrategyChange(LoadBalance localLoadBalanceStrategy) {
        //该服务的治理参数信息
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        LoadBalance newLoadBalanceStrategy = serviceGovernance.getLoadbalance();
        return !(localLoadBalanceStrategy.equals(newLoadBalanceStrategy));
    }
    
    @Override
    public void doNotify(String serviceName) {
        refresh(RefreshCallMethod.NORMAL);
    }
}
