package com.wheel.rpc.notify.impl;

import java.util.List;

import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.notify.INotify;

/**
 * 默认的变跟通知实现
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 下午3:35:13
 */
public abstract class AbstractNotify implements INotify {
    @Override
    public void serverOffline(String serviceName, String ip, int port) {
    }
    
    @Override
    public void serverOnline(String serviceName, String ip, int port) {
    }
    
    @Override
    public void serviceNodes(String serviceName, List<String> nodes) {
    }
    
    @Override
    public void serviceParamUpdate(String serviceName, ServiceGovernanceModel serviceGovernanceModel) {
    }
}
