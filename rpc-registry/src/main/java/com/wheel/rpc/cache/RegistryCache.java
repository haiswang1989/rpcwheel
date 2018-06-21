package com.wheel.rpc.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.registry.IRegistry;

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
    private static ConcurrentHashMap<String, ServiceGovernanceModel>  SERVICES_GOVERNANCE_STRATEGY = new ConcurrentHashMap<>();
    
    /** 服务与服务当前在线的服务列表 */
    private static ConcurrentHashMap<String, List<ServiceProviderNode>> SERVICES_PROVIDERS = new ConcurrentHashMap<>();    
    
    private static IRegistry registry;
    
    /**
     * 初始化服务配置
     * @param zkClient
     * @param clazzes
     */
    public static void init(List<Class<?>> clazzes) {
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
     * 结点的子结点的变化
     * @param parentPath 变化的结点
     * @param currentChilds 变化后的子结点
     */
    public static void serviceOnlineNodeChange(String serviceName, List<ServiceProviderNode> childsAfterChange) {
        List<ServiceProviderNode> currentOnlineNodes = SERVICES_PROVIDERS.get(serviceName);
        
        //TODO 上线下线
        
        
    }
    
    /**
     * 
     * @param serviceName
     * @param serviceGovernanceModel
     */
    public static void serviceParamUpdate(String serviceName, ServiceGovernanceModel serviceGovernanceModel) {
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
        }
        
        return serviceGovernanceModel;
    }
}
