package com.wheel.rpc.proxy.service.governance.failtolerance;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.model.FailTolerance;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.proxy.service.governance.failtolerance.impl.FailBack;
import com.wheel.rpc.proxy.service.governance.failtolerance.impl.FailFast;
import com.wheel.rpc.proxy.service.governance.failtolerance.impl.FailOver;
import com.wheel.rpc.proxy.service.governance.failtolerance.impl.FailSafe;
import com.wheel.rpc.proxy.service.governance.failtolerance.impl.Forking;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午5:52:30
 */
public class FailToleranceFactory {
    
    //容错策略的对象的缓存
    private static ConcurrentHashMap<String, IFailTolerance> failToleranceCache = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param failTolerance
     * @param serviceGovernance
     * @return
     */
    public static IFailTolerance createFailTolerance(FailTolerance failToleranceType, ServiceGovernanceModel serviceGovernance) {
        IFailTolerance failTolerance = null;
        switch (failToleranceType) {
            case FAILBACK:
                failTolerance = failToleranceCache.get(FailTolerance.FAILBACK.getValue());
                if(null == failTolerance) {
                    failTolerance = new FailBack();
                    failToleranceCache.put(FailTolerance.FAILBACK.getValue(), failTolerance);
                }
                break;
            case FAILFAST:
                failTolerance = failToleranceCache.get(FailTolerance.FAILFAST.getValue());
                if(null == failTolerance) {
                    failTolerance = new FailFast();
                    failToleranceCache.put(FailTolerance.FAILBACK.getValue(), failTolerance);
                }
                break;
            case FAILOVER:
                int failOverTryCount = serviceGovernance.getFailOverTryCount();
                String key = CommonUtils.appendString(FailTolerance.FAILFAST.getValue(), "_", failOverTryCount);
                failTolerance = failToleranceCache.get(key);
                if(null == failTolerance) {
                    failTolerance = new FailOver(failOverTryCount);
                    failToleranceCache.put(key, failTolerance);
                }
                break;
            case FAILSAFE:
                failTolerance = failToleranceCache.get(FailTolerance.FAILSAFE.getValue());
                if(null == failTolerance) {
                    failTolerance = new FailSafe();
                    failToleranceCache.put(FailTolerance.FAILSAFE.getValue(), failTolerance);
                }
                break;
            case FORKING:
                //并行调用的并行度
                int forkingParallelism = serviceGovernance.getForkingParallelism();
                String forkingKey = CommonUtils.appendString(FailTolerance.FORKING.getValue(), "_", forkingParallelism);
                failTolerance = failToleranceCache.get(forkingKey);
                if(null == failTolerance) {
                    failTolerance = new Forking(forkingParallelism);
                    failToleranceCache.put(forkingKey, failTolerance);
                }
                break;
            default:
                break;
        }
        
        return failTolerance;
    }
}
