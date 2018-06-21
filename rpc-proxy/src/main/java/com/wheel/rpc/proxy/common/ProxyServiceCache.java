package com.wheel.rpc.proxy.common;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.registry.IRegistry;

/**
 * 当前proxy代理的服务的缓存
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午3:56:23
 */
public class ProxyServiceCache {
    
    /** 该Proxy负责的服务列表 */
    private static List<Class<?>> proxyServices;
    
    /** 服务名称 - 服务的Provider的集合 */
    private static final ConcurrentHashMap<String, List<ServiceProviderNode>> SERVICES_PROVIDERS = new ConcurrentHashMap<>(); 
    
    /** Proxy与各个服务提供者的结点的保持的长连接 */
    private static final ConcurrentHashMap<String, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient>> SERVICES_NODES_REMOTINGCLIENTS = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param proxyServicesArgs
     * @param registry
     */
    public static void init(List<Class<?>> proxyServicesArgs, IRegistry registry) {
        proxyServices = proxyServicesArgs;
        for (Class<?> clazz : proxyServices) {
            List<ServiceProviderNode> allOnlineNodes = registry.serviceOnlineNodes(clazz.getName());
            SERVICES_PROVIDERS.put(clazz.getName(), allOnlineNodes);
        }
    }
    
    /**
     * 通过服务的名称获取服务所有的Provider
     * @param serviceName
     * @return
     */
    public static List<ServiceProviderNode> getServicesProviders(String serviceName) {
        return SERVICES_PROVIDERS.get(serviceName);
    }
    
    /**
     * 获取当前Proxy的所有的服务以及其服务的Provider
     * @return
     */
    public static ConcurrentHashMap<String, List<ServiceProviderNode>> allServicesProviders() {
        return SERVICES_PROVIDERS;
    }
    
    /**
     * 
     * @param serviceName
     * @param proxyClients
     */
    public static void setProxyServicesRemotingClients(String serviceName, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> proxyClients) {
        SERVICES_NODES_REMOTINGCLIENTS.put(serviceName, proxyClients);
    }
    
    /**
     * 获取Proxy与指定服务的指定Provider的RomtingClient
     * @param serviceName 服务名称
     * @param serviceProviderNode 服务provider
     * @return
     */
    public static NettyRemotingClient getNettyRemotingClient(String serviceName, ServiceProviderNode serviceProviderNode) {
        ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> providerNode2RomtingClient = SERVICES_NODES_REMOTINGCLIENTS.get(serviceName);
        if(null != providerNode2RomtingClient) {
            return providerNode2RomtingClient.get(serviceProviderNode);
        }
        
        return null;
    }
}
