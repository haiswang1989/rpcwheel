package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import java.util.Map;
import java.util.Random;

import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 负载均衡 - 随机算法
 * 
 * 优点:算法简单,无状态,可以很好的支持权重
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:32:32
 */
public class RandomLoadbalance extends AbstractLoadbalance {
    
    /** 生成随机数 */
    private Random random;
    
    /** 包含权重provider集合 */
    private  ServiceProviderNode[] providerAryWithWight;
    
    public RandomLoadbalance(String serviceNameArgs) {
        super(serviceNameArgs);
        this.random = new Random();
    }
    
    @Override
    public ServiceProviderNode next() {
        if(sameWeight) { //结点权重的值是一样的
            int size = allOnlineNodes.size(); 
            return allOnlineNodes.get(random.nextInt(size));
        } else { //结点的权重值不一样
            int size = providerAryWithWight.length;
            return providerAryWithWight[random.nextInt(size)];
        }
    }
    
    @Override
    public synchronized void refresh() {
        //权重不一样
        if(!sameWeight) {
            Map<ServiceProviderNode, Integer> nodesWeight = serviceGovernance.getNodesWeight();
            //权重的和
            int weightSum = 0;
            for (Integer weight : nodesWeight.values()) {
                weightSum += weight;
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
            providerAryWithWight = tempProviderAryWithWight;
        } else {
            
            
            
        }
    }
    
    /**
     * 打乱带权重Ary
     */
    private void disorganize() {
        int size = providerAryWithWight.length;
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
        ServiceProviderNode temp = providerAryWithWight[swapIndex1];
        providerAryWithWight[swapIndex1] = providerAryWithWight[swapIndex2];
        providerAryWithWight[swapIndex2] = temp;
    }
}
