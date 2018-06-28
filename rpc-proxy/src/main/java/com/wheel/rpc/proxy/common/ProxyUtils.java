package com.wheel.rpc.proxy.common;

import java.util.List;
import java.util.Map;

import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月28日 下午3:15:23
 */
public class ProxyUtils {
    
    /**
     * 判断新的提供服务的结点是否有相同的权重
     * @param allOnlineNodes
     * @param nodesWeight
     * @return
     */
    public static boolean sameWeight(List<ServiceProviderNode> allOnlineNodes, Map<ServiceProviderNode, Integer> nodesWeight) {
        boolean sameWeight = true;
        Integer baseWeight = null;
        for (ServiceProviderNode serviceProviderNode : allOnlineNodes) {
            Integer nodeWeight = nodesWeight.get(serviceProviderNode);
            int currNodeWeight = null == nodeWeight ? 0 : nodeWeight;
            if(null == baseWeight) {
                baseWeight = currNodeWeight;
            } else {
                if(baseWeight != currNodeWeight) {
                    sameWeight = false;
                    break;
                }
            }
        }
        
        return sameWeight;
    }
    
    /**
     * 是否相同权重 权重的总和
     * @param allOnlineNodes
     * @param nodesWeight
     * @return
     */
    public static Object[] sameWeightAndWeightSum(List<ServiceProviderNode> allOnlineNodes, Map<ServiceProviderNode, Integer> nodesWeight) {
        //权重的和
        int weightCount = 0;
        boolean sameWeight = true;
        Integer baseWeight = null;
        for (ServiceProviderNode serviceProviderNode : allOnlineNodes) {
            Integer nodeWeight = nodesWeight.get(serviceProviderNode);
            weightCount += nodeWeight;
            if(!sameWeight) { //如果已经不是相同权重了,下面的代码就不需要执行了,节约时间
                continue;
            }
            
            int currNodeWeight = null == nodeWeight ? 0 : nodeWeight;
            if(null == baseWeight) {
                baseWeight = currNodeWeight;
            } else {
                if(baseWeight != currNodeWeight) {
                    sameWeight = false;
                }
            }
        }
        
        return new Object[]{sameWeight, weightCount};
    }
    
}
