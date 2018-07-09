package com.wheel.rpc.core.model;

import lombok.Getter;

/**
 * 容错策略
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午5:37:55
 */
public enum FailTolerance {
    
    FAILBACK("failback"), //自动回复,将失败的请求存储在后端,定时发送
    FAILFAST("failfast"), //快速失败,只调用一次
    FAILOVER("failover"), //失败自动切换到其他的服务器,再次调用
    FAILSAFE("failsafe"), //失败安全,失败不用管
    FORKING("forking");   //并行调用,只要有一个成功,就返回
    
    FailTolerance(String value) {
        this.value = value;
    }
    
    @Getter
    private String value;
    
    private static final FailTolerance[] values = FailTolerance.values();
    
    /**
     * 
     * @param value
     * @return
     */
    public FailTolerance getLoadbalanceByValue(String value) {
        if(null!=value) {
            for (FailTolerance failTolerance : values) {
                if(value.equals(failTolerance.value)) {
                    return failTolerance;
                }
            }
        }
        
        return null;
    }
}
