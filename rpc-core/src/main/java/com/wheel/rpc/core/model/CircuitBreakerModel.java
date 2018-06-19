package com.wheel.rpc.core.model;

import lombok.Data;

/**
 * 熔断
 * 
 * 熔断策略：
 * 120秒(interval)内,请求数大于10(requestVolumeThreshold),且错误率大于60(errorPercentage)就触发熔断
 * 熔断120秒(sleepWindow)以后,进入"半熔断"状态,在半熔断时错误数>10(recoverySampleVolume) * 60%(errorPercentage) * 80%
 * 即>=4.8m,那就继续保持熔断,如果连续调用成功次数超过10(recoverySampleVolume)次,那么就熔断关闭,否则保持"半熔断"
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 下午5:20:51
 */
@Data
public class CircuitBreakerModel {
    
    private static final Integer DEFAULT_INTERVAL = 120;
    private static final Integer DEFAULT_REQUESTVOLUMETHRESHOLD = 10;
    private static final Integer DEFAULT_ERRORPERCENTAGE = 60;
    private static final Integer DEFAULT_SLEEPWINDOW = 120;
    private static final Integer DEFAULT_RECOVERYSAMPLEVOLUME = 10;
    
    /** 熔断是否开启 */
    private boolean circuitBreakerEnabled;
    
    /** 熔断器检测间隔 */
    private Integer interval = DEFAULT_INTERVAL;
    
    /** 熔断触发最低请求数 */
    private Integer requestVolumeThreshold = DEFAULT_REQUESTVOLUMETHRESHOLD;
    
    /** 熔断激活错误百分比 */
    private Integer errorPercentage = DEFAULT_ERRORPERCENTAGE;
    
    /** 熔断尝试恢复时间间隔 */
    private Integer sleepWindow = DEFAULT_SLEEPWINDOW;
    
    /** 熔断恢复检测测取样数 */
    private Integer recoverySampleVolume = DEFAULT_RECOVERYSAMPLEVOLUME;
    
    public CircuitBreakerModel(boolean circuitBreakerEnabled) {
        if(circuitBreakerEnabled == false) {
            this.circuitBreakerEnabled = false;
            this.interval = null;
            this.requestVolumeThreshold = null;
            this.errorPercentage = null;
            this.sleepWindow = null;
            this.recoverySampleVolume = null;
        } else {
            this.circuitBreakerEnabled = true;
        }
    }
}
