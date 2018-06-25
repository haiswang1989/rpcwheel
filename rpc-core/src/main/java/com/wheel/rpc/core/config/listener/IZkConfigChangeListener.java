package com.wheel.rpc.core.config.listener;

import java.util.List;

import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * ZK配置信息的Listener
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月13日 上午9:50:49
 */
public interface IZkConfigChangeListener {
    
    /**
     * 在线结点的变化
     * @param onlineNodes 新上线结点
     * @param offlineNodes 新下线结点
     */
    public void onlineNodesChange(List<ServiceProviderNode> onlineNodes, List<ServiceProviderNode> offlineNodes);
    
    /**
     * 服务治理信息的变化
     * @param oldServiceGovernanceModel
     * @param newServiceGovernanceModel
     */
    public void onGovernanceChange(ServiceGovernanceModel oldServiceGovernanceModel, ServiceGovernanceModel newServiceGovernanceModel);
}
