package com.wheel.rpc.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.I0Itec.zkclient.ZkClient;

import com.alibaba.fastjson.JSONObject;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.common.Constants;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.zookeeper.utils.ZkUtils;

/**
 * 服务治理相关的信息
 * 
 * 跟随配置中心上的配置 实时修改
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午11:45:29
 */
public class ServiceGovernanceCache {
    
    /** 服务与服务的治理参数的映射 */
    private static Map<String, ServiceGovernanceModel>  servicesGovernanceModel = new HashMap<>();
    
    /** 服务与服务当前在线的服务列表 */
    private static Map<String, List<ServiceProviderNode>> servicesOnlineNodes = new HashMap<>();
    
    /**
     * 初始化服务配置
     * @param zkClient
     * @param clazzes
     */
    public static void init(ZkClient zkClient, List<Class<?>> clazzes) {
        for (Class<?> clazz : clazzes) {
            String servicePath = CommonUtils.appendString(Constants.RPC_WHEEL_ROOT_PATH, Constants.PATH_SEPARATOR, clazz.getName());
            //治理参数
            Object data = ZkUtils.getPathData(servicePath, zkClient);
            ServiceGovernanceModel serviceGovernanceModel = null;
            if(null!=data) {
                serviceGovernanceModel = JSONObject.parseObject(String.valueOf(data), ServiceGovernanceModel.class);
            } else {
                serviceGovernanceModel = new ServiceGovernanceModel();
            }
            servicesGovernanceModel.put(clazz.getName(), serviceGovernanceModel);
            //在线服务
            servicesOnlineNodes.put(clazz.getName(), ZkUtils.getServiceOnlineNodes(servicePath, zkClient));
        }
    }
    
    
    
    /**
     * 结点的子结点的变化
     * @param parentPath 变化的结点
     * @param currentChilds 变化后的子结点
     */
    public static void childChange(String serviceName, List<String> childsAfterChange) {
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
        List<ServiceProviderNode> nodes = servicesOnlineNodes.get(serviceName);
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
        ServiceGovernanceModel serviceGovernanceModel = servicesGovernanceModel.get(serviceName);
        if(null == serviceGovernanceModel) {
            serviceGovernanceModel = new ServiceGovernanceModel();
        }
        
        return serviceGovernanceModel;
    }
}
