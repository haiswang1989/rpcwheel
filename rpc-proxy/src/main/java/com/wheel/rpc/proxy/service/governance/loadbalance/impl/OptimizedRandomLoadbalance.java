package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.common.ProxyUtils;

/**
 * 优化后的
 * 
 * 负载均衡 - 随机算法
 * 
 * 优点:算法简单,无状态,可以很好的支持权重
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:32:32
 */
public class OptimizedRandomLoadbalance extends AbstractLoadbalance {
    
    /** 生成随机数 */
    private Random random;
    
    /** provider数组,支持权重*/
    private  ServiceProviderNode[] providersArray;
    
    /** provider数组的长度 */
    private int length;
    
    public OptimizedRandomLoadbalance(String serviceNameArgs) {
        super(serviceNameArgs);
        this.random = new Random();
        create(RegistryCache.getServiceGovernance(serviceName));
    }
    
    public OptimizedRandomLoadbalance(String serviceNameArgs, List<ServiceProviderNode> availableNodes) {
        super(serviceNameArgs);
        this.random = new Random();
    }
    
    @Override
    public ServiceProviderNode next() {
        if(0 == length) {
            return null;
        }
        
        return providersArray[random.nextInt(length)];
    }
    
    @Override
    public synchronized void refresh(ServiceGovernanceModel oldServiceGovernanceModel, ServiceGovernanceModel newServiceGovernanceModel) {
        //如果负载均衡策略发生变化,直接更新负载均衡策略,然后退出
        if(oldServiceGovernanceModel.equals(newServiceGovernanceModel)) { 
            ProxyServiceCache.updateLoadbalanceStrategy(serviceName);
            return;
        }
        
        create(newServiceGovernanceModel);
    }
    
    /**
     * 
     * @param serviceGovernanceModel
     */
    private void create(ServiceGovernanceModel serviceGovernanceModel) {
        //所有的在线服务
        List<ServiceProviderNode> allOnlineNodes = RegistryCache.getOnlineNodes(serviceName);
        //服务没有在线结点
        if(0 == allOnlineNodes.size()) {
            length = 0;
            return ;
        }
        
        Map<ServiceProviderNode, Integer> nodesWeight = serviceGovernanceModel.getNodesWeight();
        boolean sameWeight = ProxyUtils.sameWeight(allOnlineNodes, nodesWeight);
        
        //权重不一样
        if(!sameWeight) {
            //权重的和
            int weightSum = 0;
            for (ServiceProviderNode serviceProviderNode : allOnlineNodes) {
                weightSum += nodesWeight.get(serviceProviderNode);
            }
            
            ServiceProviderNode[] tempProviderAryWithWight = new ServiceProviderNode[weightSum];
            int index = 0;
            for (ServiceProviderNode serviceProviderNode : allOnlineNodes) {
                int weight = nodesWeight.get(serviceProviderNode);
                while(--weight >= 0) {
                    tempProviderAryWithWight[index++] = serviceProviderNode;
                }
            }
            
            //打乱Array
            disorganize();
            //将带权重的数组指向新构造的数组中
            providersArray = tempProviderAryWithWight;
        } else {
            ServiceProviderNode[] tempProviderAryWithWight = new ServiceProviderNode[allOnlineNodes.size()];
            int index = 0;
            for (ServiceProviderNode serviceProviderNode : allOnlineNodes) {
                tempProviderAryWithWight[index++] = serviceProviderNode;
            }
            providersArray = tempProviderAryWithWight;
        }
        
        length = providersArray.length;
    }
    
    /**
     * 打乱带权重
     */
    private void disorganize() {
        int size = providersArray.length;
        for(int i=size; i>=0; i--) {
            int index = random.nextInt(i);
            swap(index, i);
        }
    }
    
    /**
     * 交换两个位置的结点
     * @param swapIndex1
     * @param swapIndex2
     */
    private void swap(int swapIndex1, int swapIndex2) {
        ServiceProviderNode temp = providersArray[swapIndex1];
        providersArray[swapIndex1] = providersArray[swapIndex2];
        providersArray[swapIndex2] = temp;
    }
}
