package com.wheel.rpc.core.config.listener.impl;

import java.util.List;

import com.wheel.rpc.core.config.listener.IZkConfigChangeListener;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月13日 上午9:53:36
 */
public abstract class AbstractZkConfigChangeListener implements IZkConfigChangeListener {
    
    @Override
    public void onlineNodesChange(List<ServiceProviderNode> onlineNodes, List<ServiceProviderNode> offlineNodes) {
    }
    
    @Override
    public void onGovernanceChange(ServiceGovernanceModel oldServiceGovernanceModel,
            ServiceGovernanceModel newServiceGovernanceModel) {
    }
}
