package com.wheel.rpc.proxy.service.governance.loadbalance.impl;

import java.util.ArrayList;

import com.wheel.rpc.core.config.listener.impl.AbstractZkConfigListener;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.service.governance.loadbalance.ILoadbalance;

/**
 * 默认的负载均衡的实现
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:31:22
 */
public abstract class AbstractLoadbalance extends AbstractZkConfigListener implements ILoadbalance {
    
    /** 服务的名称 */
    protected String serviceName;
    
    /** 所有在线服务 */
    protected ArrayList<ServiceProviderNode> allOnlineNodes;
    
    /** 当前服务的治理参数 */
    protected ServiceGovernanceModel serviceGovernance;
    
    /** 各个结点的权重是否一致 */
    protected boolean sameWeight; 
    
    public AbstractLoadbalance(String serviceNameArgs) {
        this.serviceName = serviceNameArgs;
    }
    
    @Override
    public void doNotify() {
        refresh();
    }
}
