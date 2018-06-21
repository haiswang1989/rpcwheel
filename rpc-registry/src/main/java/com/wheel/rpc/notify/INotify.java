package com.wheel.rpc.notify;

import java.util.List;

import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 对于client端的订阅,发生变化时触发的"通知"
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 下午3:26:17
 */
public interface INotify {
    
    /**
     * 变化后运行的服务的结点信息
     * @param serviceName 
     * @param nodes
     */
    public void serviceNodes(String serviceName, List<ServiceProviderNode> nodes);
    
    /**
     * 服务下线
     * @param serviceName
     * @param ip
     * @param port
     */
    public void serverOffline(String serviceName, String ip, int port);
    
    /**
     * 服务上线
     * @param serviceName
     * @param ip
     * @param port
     */
    public void serverOnline(String serviceName, String ip, int port);
    
    /**
     * 服务治理参数的变化
     * @param serviceName
     * @param serviceGovernanceModel
     */
    public void serviceParamUpdate(String serviceName, ServiceGovernanceModel serviceGovernanceModel);
}
