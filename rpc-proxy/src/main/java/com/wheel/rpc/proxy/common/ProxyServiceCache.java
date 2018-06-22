package com.wheel.rpc.proxy.common;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.service.governance.loadbalance.ILoadbalance;
import com.wheel.rpc.proxy.service.governance.loadbalance.LoadbalanceFactory;

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
    
    /** Proxy与各个服务提供者的结点的保持的长连接 */
    private static final ConcurrentHashMap<String, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient>> SERVICES_NODES_REMOTINGCLIENTS = new ConcurrentHashMap<>();
    
    /** service对应的负载均衡器 */ 
    private static final ConcurrentHashMap<String, ILoadbalance> SERVICES_LOADBALANCE_STRATEGY = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param proxyServicesArgs
     * @param registry
     */
    public static void init(List<Class<?>> proxyServicesArgs) {
        proxyServices = proxyServicesArgs;
        for (Class<?> serviceClazz : proxyServices) {
            String serviceName = serviceClazz.getName();
            initLoadbalanceStrategy(serviceName);
        }
    }
    
    /**
     * 初始化负载均衡策略
     * @param serviceName
     */
    public static void initLoadbalanceStrategy(String serviceName) {
        ILoadbalance loadbalance = LoadbalanceFactory.createLoadbalance(serviceName);
        //ILoadbalance监听配置中心的变化
        
        //TODO
        RegistryCache.addListener(loadbalance);
        SERVICES_LOADBALANCE_STRATEGY.put(serviceName, loadbalance);
    }
    
    /**
     * 更新新的负载均衡策略
     * @param serviceName
     */
    public static void updateLoadbalanceStrategy(String serviceName) {
        ILoadbalance newLoadbalance = LoadbalanceFactory.createLoadbalance(serviceName);
        //Registry中的listener
        ILoadbalance oldLoadbalance = SERVICES_LOADBALANCE_STRATEGY.get(serviceName);
        RegistryCache.removeListener(oldLoadbalance);
        RegistryCache.addListener(newLoadbalance);
        SERVICES_LOADBALANCE_STRATEGY.put(serviceName, newLoadbalance);
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
    
    /**
     * 获取服务的负载均衡器
     * @param serviceName
     * @return
     */
    public static ILoadbalance servicesLoadbalanceStrategy(String serviceName) {
        return SERVICES_LOADBALANCE_STRATEGY.get(serviceName);
    }
}
