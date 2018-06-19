package com.wheel.rpc.proxy.common;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.ZkClient;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.zookeeper.utils.ZkUtils;

/**
 * 当前proxy代理的服务的缓存
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午3:56:23
 */
public class ProxyServiceCache {
    
    private static List<Class<?>> proxyServices;
    
    /** 服务名称 - 服务的Provider的List */
    private static ConcurrentHashMap<String, List<ServiceProviderNode>> servicesProviders = new ConcurrentHashMap<>(); 
    
    /** 与服务提供者的连接 */
    private static ConcurrentHashMap<String, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient>> servicesProxyClients = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param proxyServicesArgs
     */
    public static void init(List<Class<?>> proxyServicesArgs, ZkClient zkClient) {
        proxyServices = proxyServicesArgs;
        for (Class<?> clazz : proxyServices) {
            String servicePath = CommonUtils.getServicePath(clazz.getName());
            servicesProviders.put(clazz.getName(), ZkUtils.getServiceOnlineNodes(servicePath, zkClient));
        }
    }
    
    /**
     * 
     * @param serviceName
     * @return
     */
    public static List<ServiceProviderNode> getServicesProviders(String serviceName) {
        return servicesProviders.get(serviceName);
    }
    
    /**
     * 
     * @return
     */
    public static ConcurrentHashMap<String, List<ServiceProviderNode>> getServicesProviders() {
        return servicesProviders;
    }
    
    /**
     * 
     * @param serviceName
     * @param proxyClients
     */
    public static void putServicesProxyClients(String serviceName, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> proxyClients) {
        servicesProxyClients.put(serviceName, proxyClients);
    }
    
    /**
     * 
     * @param serviceName
     * @param serviceProviderNode
     * @return
     */
    public static NettyRemotingClient getNettyRemotingClient(String serviceName, ServiceProviderNode serviceProviderNode) {
        return servicesProxyClients.get(serviceName).get(serviceProviderNode);
    }
}
