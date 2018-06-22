package com.wheel.rpc.core.cache;

import com.wheel.rpc.core.config.listener.IZkConfigChangeListener;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月22日 下午5:21:09
 */
public interface ICache extends IZkConfigChangeListener {

    public void reflesh(String serviceName);
    
}
