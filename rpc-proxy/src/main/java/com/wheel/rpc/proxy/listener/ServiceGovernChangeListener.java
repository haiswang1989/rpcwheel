package com.wheel.rpc.proxy.listener;

import com.wheel.rpc.core.config.listener.impl.AbstractZkConfigChangeListener;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.service.governance.ILoadbalance;
import com.wheel.rpc.proxy.common.ProxyServiceCache;

/**
 * 服务治理信息的变化处理
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 下午4:01:44
 */
public class ServiceGovernChangeListener extends AbstractZkConfigChangeListener {
    
    private String serviceName;
    
    public ServiceGovernChangeListener(String serviceNameArgs) {
        this.serviceName = serviceNameArgs;
    }
    
    @Override
    public void onGovernanceChange(ServiceGovernanceModel oldServiceGovernanceModel,
            ServiceGovernanceModel newServiceGovernanceModel) {
        ILoadbalance serviceLoadbalance = ProxyServiceCache.servicesLoadbalance(serviceName);
        serviceLoadbalance.refresh(oldServiceGovernanceModel, newServiceGovernanceModel);
    }
}
