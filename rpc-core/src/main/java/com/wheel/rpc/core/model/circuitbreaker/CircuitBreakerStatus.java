package com.wheel.rpc.core.model.circuitbreaker;

/**
 * 熔断的状态
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月29日 下午5:42:02
 */
public enum CircuitBreakerStatus {
    
    OPEN/*熔断打开,停止调用*/, CLOSED/*熔断关闭,服务正常调用*/, HALF_OPEN/*熔断半打开,放出少量的请求*/
}
