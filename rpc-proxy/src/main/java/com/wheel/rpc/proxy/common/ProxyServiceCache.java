package com.wheel.rpc.proxy.common;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.service.governance.ILoadbalance;
import com.wheel.rpc.proxy.listener.OnlineNodesChangeListener;
import com.wheel.rpc.proxy.listener.ServiceGovernChangeListener;
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
    
    /** servicename - 被熔断的服务列表 */
    private static final ConcurrentHashMap<String, List<ServiceProviderNode>> SERVICES_CIRCUITBREAKER_PROVIDERS = new ConcurrentHashMap<>();
    
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
        SERVICES_LOADBALANCE_STRATEGY.put(serviceName, loadbalance);
        //监听Provider结点的上下线
        RegistryCache.addListener(serviceName, new OnlineNodesChangeListener(serviceName));
        //监听服务的治理参数的变化
        RegistryCache.addListener(serviceName, new ServiceGovernChangeListener(serviceName));
    }
    
    /**
     * 重新初始化负载均衡策略
     * @param serviceName
     */
    public static void updateLoadbalanceStrategy(String serviceName) {
        ILoadbalance newLoadbalance = LoadbalanceFactory.createLoadbalance(serviceName);
        //Registry中的listener
        SERVICES_LOADBALANCE_STRATEGY.put(serviceName, newLoadbalance);
    }
    
    /**
     * 设置服务Proxy与服务的Provider的连接对象
     * @param serviceName
     * @param proxyClients
     */
    public static void setServiceClients(String serviceName, ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> proxyClients) {
        SERVICES_NODES_REMOTINGCLIENTS.put(serviceName, proxyClients);
    }
    
    /**
     * 获取Proxy与指定服务的指定Provider的连接
     * @param serviceName 服务名称
     * @param serviceProviderNode 服务provider
     * @return
     */
    public static NettyRemotingClient getServiceTargetClient(String serviceName, ServiceProviderNode serviceProviderNode) {
        ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> providerNode2RomtingClient = SERVICES_NODES_REMOTINGCLIENTS.get(serviceName);
        if(null != providerNode2RomtingClient) {
            return providerNode2RomtingClient.get(serviceProviderNode);
        }
        
        return null;
    }
    
    /**
     * 获取服务,所有在线结点的连接信息
     * @param serviceName
     * @return
     */
    public static ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> serviceClients(String serviceName) {
        ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> allRomtingClients = SERVICES_NODES_REMOTINGCLIENTS.get(serviceName);
        if(null == allRomtingClients) {
            allRomtingClients = new ConcurrentHashMap<>();
            SERVICES_NODES_REMOTINGCLIENTS.put(serviceName, allRomtingClients);
        }
        
        return allRomtingClients;
    }
    
    /**
     * 获取服务的负载均衡器
     * @param serviceName
     * @return
     */
    public static ILoadbalance servicesLoadbalance(String serviceName) {
        return SERVICES_LOADBALANCE_STRATEGY.get(serviceName);
    }
    
    /**
     * 获取服务的熔断列表
     * @param serviceName
     * @return
     */
    public static List<ServiceProviderNode> getCircuitbreakerProviders(String serviceName) {
        List<ServiceProviderNode> circuitbreakerProviders = SERVICES_CIRCUITBREAKER_PROVIDERS.get(serviceName);
        if(null == circuitbreakerProviders) {
            circuitbreakerProviders = new ArrayList<>();
            SERVICES_CIRCUITBREAKER_PROVIDERS.put(serviceName, circuitbreakerProviders);
        }
        
        return circuitbreakerProviders;
    }
}
