package com.wheel.rpc.container.limiter;

import com.google.common.util.concurrent.RateLimiter;

/**
 * 限流
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月26日 下午5:13:05
 */
public class Limiter {
    
    private static final long WAIT_MILLIS=1000L;
    
    private RateLimiter rateLimiter;
    
    /** 创建时间 */
    private long createDate;
    
    private volatile boolean usable = false;
    
    public Limiter(int limiter) {
        rateLimiter = RateLimiter.create(limiter);
        createDate = System.currentTimeMillis();
    }
    
    /**
     * 
     * @return
     */
    public boolean tryAcquire() {
        boolean allow = rateLimiter.tryAcquire();
        if(!allow) {
            if(!usable) {
                if(System.currentTimeMillis() - createDate >= WAIT_MILLIS) { 
                    usable = true;
                } else { //启动1S以内的放过所有的请求
                    return true; 
                }
            }
        }
        
        return allow;
    }
}
