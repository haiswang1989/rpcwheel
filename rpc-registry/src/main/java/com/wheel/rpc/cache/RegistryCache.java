package com.wheel.rpc.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import com.wheel.rpc.core.config.bean.ServiceConfigBean;
import com.wheel.rpc.core.config.listener.IZkConfigChangeListener;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.registry.IRegistry;

import lombok.Setter;

/**
 * 服务治理相关的信息
 * 
 * 跟随配置中心上的配置 实时修改
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午11:45:29
 */
public class RegistryCache {
    
    /** 服务与服务的治理参数的映射 */
    private static final ConcurrentHashMap<String, ServiceGovernanceModel>  SERVICES_GOVERNANCE_STRATEGY = new ConcurrentHashMap<>();
    
    /** 服务与服务当前在线的服务列表 */
    private static final ConcurrentHashMap<String, List<ServiceProviderNode>> SERVICES_PROVIDERS = new ConcurrentHashMap<>();    
    
    /** 注册中心 */
    @Setter
    private static IRegistry registry;
    
    /**对ZK配置变化 感兴趣的listener  serviceName和listener的映射 */
    private static ConcurrentHashMap<String, CopyOnWriteArraySet<IZkConfigChangeListener>> service2Listeners = new ConcurrentHashMap<>();
    
    /**
     * 初始化服务配置
     * @param zkClient
     * @param clazzes
     */
    public static void initByClasses(List<Class<?>> clazzes) {
        check();
        for (Class<?> clazz : clazzes) {
            ServiceGovernanceModel serviceGovernanceModel = registry.serviceGovernanceStrategy(clazz.getName());
            //服务的治理信息
            SERVICES_GOVERNANCE_STRATEGY.put(clazz.getName(), serviceGovernanceModel);
            List<ServiceProviderNode> allOnlineNodes = registry.serviceOnlineNodes(clazz.getName());
            //服务的在线结点
            SERVICES_PROVIDERS.put(clazz.getName(), allOnlineNodes);
        }
    }
    
    /**
     * 初始化服务配置
     * @param zkClient
     * @param clazzes
     */
    public static void initByServiceConfigBean(List<ServiceConfigBean<?>> serviceConfigBeans) {
        check();
        for (ServiceConfigBean<?> serviceConfigBean : serviceConfigBeans) {
            Class<?> clazz = serviceConfigBean.getInterfaceClazz();
            ServiceGovernanceModel serviceGovernanceModel = registry.serviceGovernanceStrategy(clazz.getName());
            //服务的治理信息
            SERVICES_GOVERNANCE_STRATEGY.put(clazz.getName(), serviceGovernanceModel);
            List<ServiceProviderNode> allOnlineNodes = registry.serviceOnlineNodes(clazz.getName());
            //服务的在线结点
            SERVICES_PROVIDERS.put(clazz.getName(), allOnlineNodes);
        }
    }
    
    /**
     * 
     */
    private static void check() {
        if(null == registry) {
            throw new RpcException("Registry can not be null.");
        }
    }
    
    
    
    /**
     * 结点的子结点的变化
     * @param parentPath 变化的结点
     * @param newOnlineNodes 变化后的子结点
     */
    public static void serviceOnlineNodeChange(String serviceName, List<ServiceProviderNode> newOnlineNodes) {
        List<ServiceProviderNode> currentOnlineNodes = SERVICES_PROVIDERS.get(serviceName);
        List<ServiceProviderNode> currentOnlineNodesCopy = new ArrayList<>(currentOnlineNodes);
        List<ServiceProviderNode> newOnlineNodesCopy = new ArrayList<>(newOnlineNodes);
        
        //下线结点
        currentOnlineNodesCopy.removeAll(newOnlineNodes);
        //上线结点
        newOnlineNodesCopy.removeAll(currentOnlineNodes);
        if(0!=currentOnlineNodesCopy.size() || 0!=newOnlineNodesCopy.size()) {
            //结点发生了变化,需要通知
            //更新缓存
            SERVICES_PROVIDERS.put(serviceName, newOnlineNodes);
            CopyOnWriteArraySet<IZkConfigChangeListener> listeners = service2Listeners.get(serviceName);
            if(null!=listeners) {
                for (IZkConfigChangeListener listener : listeners) {
                    listener.onlineNodesChange(newOnlineNodesCopy, currentOnlineNodesCopy);
                }
            }
        } 
    }
    
    /**
     * 
     * @param serviceName
     * @param serviceGovernanceModel
     */
    public static void serviceParamUpdate(String serviceName, ServiceGovernanceModel serviceGovernanceModel) {
        ServiceGovernanceModel oldServiceGovernanceModel = SERVICES_GOVERNANCE_STRATEGY.get(serviceName);
        //更新缓存
        SERVICES_GOVERNANCE_STRATEGY.put(serviceName, serviceGovernanceModel);
        CopyOnWriteArraySet<IZkConfigChangeListener> listeners = service2Listeners.get(serviceName);
        if(null!=listeners) {
            for (IZkConfigChangeListener listener : listeners) {
                listener.onGovernanceChange(oldServiceGovernanceModel, serviceGovernanceModel);
            }
        }
    }
    
    /**
     * 获取服务在线的结点
     * @param serviceName
     * @return
     */
    public static List<ServiceProviderNode> getOnlineNodes(String serviceName) {
        List<ServiceProviderNode> nodes = SERVICES_PROVIDERS.get(serviceName);
        if(null == nodes) {
            nodes = new ArrayList<>();
            SERVICES_PROVIDERS.put(serviceName, nodes);
        }
        
        return nodes;
    }
    
    /**
     * 
     * @param serviceName
     * @return
     */
    public static ServiceGovernanceModel getServiceGovernance(String serviceName) {
        ServiceGovernanceModel serviceGovernanceModel = SERVICES_GOVERNANCE_STRATEGY.get(serviceName);
        if(null == serviceGovernanceModel) {
            serviceGovernanceModel = new ServiceGovernanceModel();
            SERVICES_GOVERNANCE_STRATEGY.put(serviceName, serviceGovernanceModel);
        }
        
        return serviceGovernanceModel;
    }
    
    /**
     * 
     * @return
     */
    public static ConcurrentHashMap<String, List<ServiceProviderNode>> allServicesProviders() {
        return SERVICES_PROVIDERS;
    }
    
    /**
     * 添加service的listener
     * @param serviceName
     * @param needAddListener
     */
    public static void addListener(String serviceName, IZkConfigChangeListener needAddListener) {
        CopyOnWriteArraySet<IZkConfigChangeListener> listeners = service2Listeners.get(serviceName);
        if(null == listeners) {
            listeners = new CopyOnWriteArraySet<>();
            service2Listeners.put(serviceName, listeners);
        }
        
        listeners.add(needAddListener);
    }
    
    /**
     * 删除service的listener
     * @param serviceName
     * @param needRemoveListener
     */
    public static void removeListener(String serviceName, IZkConfigChangeListener needRemoveListener) {
        CopyOnWriteArraySet<IZkConfigChangeListener> listeners = service2Listeners.get(serviceName);
        if(null!=listeners) {
            listeners.remove(needRemoveListener);
        }
    }
}
