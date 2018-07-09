package com.wheel.rpc.proxy.service.governance.failtolerance.impl;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.LoadBalance;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.service.governance.ILoadbalance;
import com.wheel.rpc.proxy.service.governance.failtolerance.IFailTolerance;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.NormalRandomLoadbalance;

/**
 * 容错策略的抽象实现
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午2:28:38
 */
public abstract class AbstractFailTolerance implements IFailTolerance {
    
    /**
     * 
     * @param rpcRequest
     * @return
     */
    public ILoadbalance getLoadbalance(RpcRequest rpcRequest) {
        ILoadbalance loadbalance = null;
        if(rpcRequest.isCanOptimize()) {
            //有路由的
            loadbalance = newLoadbalance(rpcRequest);
        } else {
            //无路由的,优化过的
            loadbalance = rpcRequest.getLoadbalance();
        }
        
        return loadbalance;
    }
    
    /**
     * 
     * @param rpcRequest
     * @return
     */
    private ILoadbalance newLoadbalance(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        LoadBalance loadBalanceType = serviceGovernance.getLoadbalanceType();
        ILoadbalance loadbalance = null;
        switch (loadBalanceType) {
            case RANDOM:
                loadbalance = new NormalRandomLoadbalance(rpcRequest.getAvailableProviders(), serviceName);
                break;
            default:
                break;
        }
        
        return loadbalance;
    }
}
