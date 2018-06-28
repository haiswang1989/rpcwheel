package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.common.ProxyUtils;

/**
 * 普通的
 * 
 * 负载均衡 - 随机算法
 * 
 * 优点:算法简单,无状态,可以很好的支持权重
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月28日 下午3:43:04
 */
public class NormalRandomLoadbalance extends AbstractLoadbalance {
    
    /** 用于获取随机数 */
    private Random random;
    
    /** 经过路由以后有效的Provider */
    private List<ServiceProviderNode> availableProviders;
    
    /** Provider的权重 */
    private Map<ServiceProviderNode, Integer> nodesWeight;
    
    /** 有效Provider的权重是否相同 */
    private boolean sameWeight;
    
    /** 有效Provider的权重总和 */
    private int weightSum;
    
    public NormalRandomLoadbalance(List<ServiceProviderNode> availableProvidersArgs, String serviceNameArgs) {
        super(serviceNameArgs);
        this.availableProviders = availableProvidersArgs;
        this.random = new Random();
        init();
    }
    
    /**
     * 初始化
     */
    private void init() {
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        List<ServiceProviderNode> allOnlineNodes = RegistryCache.getOnlineNodes(serviceName);
        availableProviders.retainAll(allOnlineNodes);
        nodesWeight = serviceGovernance.getNodesWeight();
        Object[] sameWeightAndWeightSum = ProxyUtils.sameWeightAndWeightSum(availableProviders, nodesWeight);
        sameWeight = (boolean)sameWeightAndWeightSum[0];
        weightSum = (int)sameWeightAndWeightSum[1];
    }
    
    /**
     * 
     * @return
     */
    public ServiceProviderNode next() {
        if(sameWeight) {
            int index = random.nextInt(availableProviders.size());
            return availableProviders.get(index);
        } else {
            int indexWithWeight = random.nextInt(weightSum);
            for (ServiceProviderNode serviceProviderNode : availableProviders) {
                int weight = nodesWeight.get(serviceProviderNode);
                indexWithWeight -= weight;
                if(indexWithWeight <= 0) {
                    return serviceProviderNode;
                }
            }
        }
        
        //not reach
        return null;
    }
}
