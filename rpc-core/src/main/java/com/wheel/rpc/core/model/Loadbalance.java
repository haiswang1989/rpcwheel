package com.wheel.rpc.core.model;

import lombok.Getter;

/**
 * 负载均衡策略
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 下午4:46:59
 */
public enum LoadBalance {
    HASH("Hash"), //一致性Hash
    RANDOM("Random"), //随机
    LEASTLOAD("LeastLoad"), //最少在途请求
    ROUNDROBIN("RoundRobin"); //轮训
    
    private static final LoadBalance[] values = LoadBalance.values();
    
    @Getter
    private String value;
    
    LoadBalance(String value) {
        this.value = value;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public LoadBalance getLoadbalanceByValue(String value) {
        if(null!=value) {
            for (LoadBalance loadbalance : values) {
                if(value.equals(loadbalance.value)) {
                    return loadbalance;
                }
            }
        }
        
        return null;
    }
}
