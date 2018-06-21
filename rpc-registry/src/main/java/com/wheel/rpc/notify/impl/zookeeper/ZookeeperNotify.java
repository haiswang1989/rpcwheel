package com.wheel.rpc.notify.impl.zookeeper;

import java.util.List;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.notify.impl.AbstractNotify;

import lombok.Getter;

/**
 * zookeeper 变跟的通知实现
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 下午3:36:05
 */
public class ZookeeperNotify extends AbstractNotify {
    
    /** 服务上下线监控 */
    @Getter
    private ZkNodeChangeWatcher nodeChangeWatcher;
    
    /** 服务结点的治理参数的监控 */
    @Getter
    private ZkNodeDataChangeWatcher serviceGovernanceDataChangeWatcher;
    
    /** 服务的标识 */
    @Getter
    public String serviceName;
    
    public ZookeeperNotify(String serviceNameArgs) {
        this.serviceName = serviceNameArgs;
        nodeChangeWatcher = new ZkNodeChangeWatcher(this);
        serviceGovernanceDataChangeWatcher = new ZkNodeDataChangeWatcher(this);
    }

    @Override
    public void serviceNodes(String serviceName, List<ServiceProviderNode> nodes) {
        RegistryCache.serviceOnlineNodeChange(serviceName, nodes);
    }
    
    @Override
    public void serviceParamUpdate(String serviceName, ServiceGovernanceModel serviceGovernanceModel) {
        RegistryCache.serviceParamUpdate(serviceName, serviceGovernanceModel);
    }
}
