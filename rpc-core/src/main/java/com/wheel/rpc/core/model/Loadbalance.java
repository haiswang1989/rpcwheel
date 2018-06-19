package com.wheel.rpc.core.model;

import lombok.Getter;

/**
 * 负载均衡策略
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 下午4:46:59
 */
public enum Loadbalance {
    HASH("Hash"), //一致性Hash
    RANDOM("Random"), //随机
    LEASTLOAD("LeastLoad"), //最少在途请求
    ROUNDROBIN("RoundRobin"); //轮训
    
    private static final Loadbalance[] values = Loadbalance.values();
    
    @Getter
    private String value;
    
    Loadbalance(String value) {
        this.value = value;
    }
    
    /**
     * 
     * @param value
     * @return
     */
    public Loadbalance getLoadbalanceByValue(String value) {
        if(null!=value) {
            for (Loadbalance loadbalance : values) {
                if(value.equals(loadbalance.value)) {
                    return loadbalance;
                }
            }
        }
        
        return null;
    }
}
