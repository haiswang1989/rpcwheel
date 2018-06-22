package com.wheel.rpc.proxy.service.governance.loadbalance;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.LoadBalance;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.HashLoadbalance;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.LeastLoadLoadbalance;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.RandomLoadbalance;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.RoundRobinLoadbalance;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月22日 下午4:03:46
 */
public class LoadbalanceFactory {
    
    /**
     * 创建服务的负载均衡器
     * @param serviceName
     * @param loadBalanceStrategy
     * @return
     */
    public static ILoadbalance createLoadbalance(String serviceName) {
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        LoadBalance loadBalanceStrategy = serviceGovernance.getLoadbalance();
        ILoadbalance loadBalance = null;
        switch (loadBalanceStrategy) {
            case HASH:
                loadBalance = new HashLoadbalance(serviceName);
                break;
            case RANDOM:
                loadBalance = new RandomLoadbalance(serviceName);
                break;
            case LEASTLOAD:
                loadBalance = new LeastLoadLoadbalance(serviceName);
                break;
            case ROUNDROBIN:
                loadBalance = new RoundRobinLoadbalance(serviceName);
                break; 
            default:
                break;
        }
        
        return loadBalance;
    }
    
}
