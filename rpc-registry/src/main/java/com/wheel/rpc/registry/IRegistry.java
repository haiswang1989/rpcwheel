package com.wheel.rpc.registry;

import java.util.List;

import com.wheel.rpc.core.model.RegistryModel;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.notify.INotify;

/**
 * 注册中心接口
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 上午11:50:50
 */
public interface IRegistry {
    
    /**
     * 向注册中心注册服务
     * @param registryModel
     */
    public void regist(RegistryModel registryModel);
    
    /**
     * 向注册中卸载服务
     * @param registryModel
     */
    public void unregist(RegistryModel registryModel);
    
    /**
     * client端向注册中心"订阅"服务相关变化
     * @return
     */
    public void subscribe(INotify notify);
    
    /**
     * client端向注册中心"取消订阅"服务相关变化
     * @return
     */
    public void unsubscribe(INotify notify);
    
    /**
     * 获取服务的在线结点
     * @param serviceName
     * @return
     */
    public List<ServiceProviderNode> serviceOnlineNodes(String serviceName);
    
    /**
     * 服务的治理策略
     * @param serviceName
     * @return
     */
    public ServiceGovernanceModel serviceGovernanceStrategy(String serviceName);
    
    /**
     * 关闭与配置中心的连接
     */
    public void close();
}
