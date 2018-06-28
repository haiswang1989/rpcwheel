package com.wheel.rpc.registry.impl.zookeeper;

import java.net.InetSocketAddress;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkInterruptedException;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.common.Constants;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.model.RegistryModel;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.zookeeper.utils.ZkUtils;
import com.wheel.rpc.notify.INotify;
import com.wheel.rpc.notify.impl.zookeeper.ZkNodeChangeWatcher;
import com.wheel.rpc.notify.impl.zookeeper.ZkNodeDataChangeWatcher;
import com.wheel.rpc.notify.impl.zookeeper.ZookeeperNotify;
import com.wheel.rpc.registry.impl.AbstractRegistry;

/**
 * Zookeeper 的注册中心的实现
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月4日 下午5:49:15
 */
public class ZookeeperRegistry extends AbstractRegistry {
    
    /** 与注册中心的连接 */
    private ZkClient zkClient;
    
    private static volatile ZookeeperRegistry INSTANCE;
    
    private ZookeeperRegistry(RegistryConfigBean registryConfigBean) {
        this.zkClient = new ZkClient(registryConfigBean.getConnection());
    }
    
    public static ZookeeperRegistry getInstance(RegistryConfigBean registryConfigBean) {
        if(null==INSTANCE) {
            synchronized(ZookeeperRegistry.class) {
                if(null==INSTANCE) {
                    INSTANCE = new ZookeeperRegistry(registryConfigBean);
                }
            }
        }
        
        return INSTANCE;
    }
    
    @Override
    public void regist(RegistryModel registryModel) {
        //服务治理相关参数的MODEL
        ServiceGovernanceModel serviceGovernanceModel = registryModel.getServiceGovernanceModel();
        //将服务治理对象转换成JSON String
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        
        String intfClassName = registryModel.getService().getName();
        InetSocketAddress address = registryModel.getAddress();
        String hostname = address.getHostName();
        int port = address.getPort();
        
        //服务目录是否存在 {rootPath}/{serviceName}
        String servicePath = CommonUtils.getServicePath(intfClassName);
        if(!ZkUtils.exists(servicePath, zkClient)) {
            ZkUtils.createPersistentRecursion(servicePath, zkClient);
            ZkUtils.writeData(servicePath, data, zkClient);
        }
        
        //服务提供者结点的名称ip:port {rootPath}/{serviceName}/ip:port
        String providerNodeName = CommonUtils.appendString(hostname, Constants.IP_PORT_CONNECTOR_CHAR, port);
        //服务提供者结点的完整路径
        String serviceProviderPath = CommonUtils.appendString(servicePath, Constants.PATH_SEPARATOR, providerNodeName);
        //注册服务提供者
        ZkUtils.registServiceProvider(serviceProviderPath, "", zkClient);
    }

    @Override
    public void unregist(RegistryModel registryModel) {
        String intfClassName = registryModel.getService().getName();
        InetSocketAddress address = registryModel.getAddress();
        String hostname = address.getHostName();
        int port = address.getPort();
        
        //服务目录是否存在 {rootPath}/{serviceName}
        String servicePath = CommonUtils.getServicePath(intfClassName);
        //服务提供者结点的名称ip:port {rootPath}/{serviceName}/ip:port
        String providerNodeName = CommonUtils.appendString(hostname, Constants.IP_PORT_CONNECTOR_CHAR, port);
        //服务提供者结点的完整路径
        String serviceProviderPath = CommonUtils.appendString(servicePath, Constants.PATH_SEPARATOR, providerNodeName);
        //卸载服务的提供者
        ZkUtils.unRegistServiceProvider(serviceProviderPath, zkClient);
    }

    @Override
    public void subscribe(INotify notify) {
        ZookeeperNotify zkNotify = (ZookeeperNotify)notify;
        String serviceName = zkNotify.getServiceName();
        ZkNodeChangeWatcher zkNodeChangeWatcher = zkNotify.getNodeChangeWatcher();
        ZkNodeDataChangeWatcher serviceGovernanceDataChangeWatcher = zkNotify.getServiceGovernanceDataChangeWatcher();
        subscribe(serviceName, zkNodeChangeWatcher, serviceGovernanceDataChangeWatcher);
    }
    
    /**
     * 发布订阅
     * 
     * 1：订阅服务的上下线
     * 2：订阅服务提供的结点的"治理参数"的变化
     * 
     * @param serviceName 服务名称
     * @param zkNodeChangeWatcher 服务上下线的订阅
     * @param serviceGovernanceDataChangeWatcher 服务结点的"治理参数"的订阅
     */
    private void subscribe(String serviceName, ZkNodeChangeWatcher zkNodeChangeWatcher, ZkNodeDataChangeWatcher serviceGovernanceDataChangeWatcher) {
        String servicePath = CommonUtils.getServicePath(serviceName);
        //服务结点上下线的监控
        ZkUtils.subscribeServiceOnOffline(servicePath, zkNodeChangeWatcher, zkClient);
        //服务的治理参数的变化的监控
        ZkUtils.subscribeServiceParamChange(servicePath, serviceGovernanceDataChangeWatcher, zkClient);
    }
    
    @Override
    public void unsubscribe(INotify notify) {
        ZookeeperNotify zkNotify = (ZookeeperNotify)notify;
        String serviceName = zkNotify.getServiceName();
        ZkNodeChangeWatcher zkNodeChangeWatcher = zkNotify.getNodeChangeWatcher();
        ZkNodeDataChangeWatcher serviceGovernanceDataChangeWatcher = zkNotify.getServiceGovernanceDataChangeWatcher();
        unsubscribe(serviceName, zkNodeChangeWatcher, serviceGovernanceDataChangeWatcher);
    }
    
    /**
     * 取消订阅
     * 1：取消订阅服务结点的上下线
     * 2：取消订阅服务"治理参数"的变化
     * 
     * @param serviceName
     * @param zkNodeChangeWatcher
     * @param zkNodeDataChangeWatcher
     */
    private void unsubscribe(String serviceName, ZkNodeChangeWatcher zkNodeChangeWatcher, ZkNodeDataChangeWatcher serviceGovernanceDataChangeWatcher) {
        String servicePath = CommonUtils.getServicePath(serviceName);
        //取消订阅服务结点的上下线
        ZkUtils.unSubscribeServiceOnOffline(servicePath, zkNodeChangeWatcher, zkClient);
        ZkUtils.unSubscribeServiceParamChange(servicePath, serviceGovernanceDataChangeWatcher, zkClient);
    }
    
    @Override
    public List<ServiceProviderNode> serviceOnlineNodes(String serviceName) {
        //服务在zk上面绝对路径
        String servicePath = CommonUtils.getServicePath(serviceName);
        List<String> onlineNodes = zkClient.getChildren(servicePath);
        List<ServiceProviderNode> onlineProviderNodes = Lists.newArrayListWithCapacity(onlineNodes.size());
        for (String onlineNode : onlineNodes) {
            onlineProviderNodes.add(new ServiceProviderNode(onlineNode));
        }
        
        return onlineProviderNodes;
    }
    
    @Override
    public ServiceGovernanceModel serviceGovernanceStrategy(String serviceName) {
        String servicePath = CommonUtils.getServicePath(serviceName);
        String json = zkClient.readData(servicePath, true);
        if(null == json) {
            return new ServiceGovernanceModel();
        }
        
        return JSONObject.parseObject(json, ServiceGovernanceModel.class);
    }
    
    @Override
    public void close() {
        try {
            if(null != zkClient) {
                zkClient.close();
            }
        } catch (ZkInterruptedException e) {
        }
    }
}
