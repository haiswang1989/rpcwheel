package com.wheel.rpc.core.config.listener;

/**
 * ZK配置信息的Listener
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月13日 上午9:50:49
 */
public interface IZkConfigChangeListener {
    
    public void doNotify(String serviceName);
    
}
