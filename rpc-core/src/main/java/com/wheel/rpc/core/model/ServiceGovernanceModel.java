package com.wheel.rpc.core.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * 服务治理相关信息
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 下午2:28:56
 */
@Data
public class ServiceGovernanceModel {
    
    /** 方法默认的超时时间, 400ms*/
    private static final Long DEFAULT_METHOD_TIMEOUT_THRESHOLD = 400L;
    
    /** 方法不限流 */
    private static final int METHOD_RATELIMITER_UNLIMITED = -1;
    
    /** 方法的超时 */
    /** method / 超时阈值 */
    private Map<String, Long> methodsTimeout = new HashMap<>();
    
    /** 负载均衡策略 - proxy */
    private LoadBalance loadbalance = LoadBalance.RANDOM;
    
    /** 是否降级 - server */
    public boolean isDegradation = false;
    
    /** 方法的限流 - server */
    /** method / 每秒调用阈值 */
    private Map<String, Integer> methodsRateLimiter = new HashMap<>();
    
    /** 熔断策略 - proxy*/
    private CircuitBreakerModel circuitBreakerModel = new CircuitBreakerModel(false);
    
    /** 结点的权重 - proxy*/
    private Map<ServiceProviderNode, Integer> nodesWeight = new HashMap<>();
    
    public ServiceGovernanceModel() {
    }
    
    /**
     * 获取方法的限流参数,如果没有设置那么就是unlimited(返回-1)
     * 否则按配置的返回
     * @param methodName
     * @return
     */
    public int getRateLimiter(String methodName) {
        Integer rateLimiter = methodsRateLimiter.get(methodName);
        return null==rateLimiter ? METHOD_RATELIMITER_UNLIMITED : rateLimiter;
    }
    
    /**
     * 获取方法的超时参数,如果没有设置,那么返回默认值DEFAULT_METHOD_TIMEOUT_THRESHOLD
     * 
     * @param methodName
     * @return
     */
    public long getTimeout(String methodName) {
        Long timeout = methodsTimeout.get(methodName);
        return null==timeout ? DEFAULT_METHOD_TIMEOUT_THRESHOLD : timeout;
    }
}
