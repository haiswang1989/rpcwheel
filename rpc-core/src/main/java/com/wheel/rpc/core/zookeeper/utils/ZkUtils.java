package com.wheel.rpc.core.zookeeper.utils;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import com.google.common.collect.Lists;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * zookeeper操作辅助类
 * 
 * 1:在zookeeper中,每一个路径对应着树中的一个结点
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月5日 上午9:20:29
 */
public class ZkUtils {
    
    /**
     * "结点"是否存在
     * @param path
     * @param zkClient
     * @return
     */
    public static boolean exists(String path, ZkClient zkClient) {
        return zkClient.exists(path);
    }
    
    /**
     * 创建结点,如果父结点不存在递归创建父节点
     * @param path
     * @param zkClient
     */
    public static void createPersistentRecursion(String path, ZkClient zkClient) {
        zkClient.createPersistent(path, true);
    }
    
    /**
     * 订阅服务"上下线"
     * @param path
     * @param zkNodeChangeWatcher
     * @param zkClient
     */
    public static void subscribeServiceOnOffline(String path, IZkChildListener zkNodeChangeWatcher, ZkClient zkClient) {
        zkClient.subscribeChildChanges(path, zkNodeChangeWatcher);
    }
    
    /**
     * 取消订阅服务的"上下线"
     * @param path
     * @param zkNodeChangeWatcher
     * @param zkClient
     */
    public static void unSubscribeServiceOnOffline(String path, IZkChildListener zkNodeChangeWatcher, ZkClient zkClient) {
        zkClient.unsubscribeChildChanges(path, zkNodeChangeWatcher);
    }
    
    
    /**
     * 订阅"服务治理参数"的变化
     * @param path
     * @param zkNodeDataChangeWatcher
     * @param zkClient
     */
    public static void subscribeServiceParamChange(String path, IZkDataListener zkNodeDataChangeWatcher, ZkClient zkClient) {
        zkClient.subscribeDataChanges(path, zkNodeDataChangeWatcher);
    }
    
    /**
     * 取消订阅"服务治理参数"的变化
     * @param path
     * @param zkNodeDataChangeWatcher
     * @param zkClient
     */
    public static void unSubscribeServiceParamChange(String path, IZkDataListener zkNodeDataChangeWatcher, ZkClient zkClient) {
        zkClient.unsubscribeDataChanges(path, zkNodeDataChangeWatcher);
    }
    
    /**
     * 获取服务在线的结点
     * @param path
     * @param zkClient
     * @return
     */
    public static List<ServiceProviderNode> getServiceOnlineNodes(String path, ZkClient zkClient) {
        List<String> onlineNodes = zkClient.getChildren(path);
        List<ServiceProviderNode> onlineProviderNodes = Lists.newArrayListWithCapacity(onlineNodes.size());
        for (String onlineNode : onlineNodes) {
            onlineProviderNodes.add(new ServiceProviderNode(onlineNode));
        }
        return onlineProviderNodes;
    }
    
    /**
     * 注册服务提供者
     * @param path
     * @param data
     * @param zkClient
     */
    public static void registServiceProvider(String path, Object data, ZkClient zkClient) {
        zkClient.createEphemeral(path, data);
    }
    
    /**
     * 卸载服务提供者
     * @param path
     * @param zkClient
     */
    public static boolean unRegistServiceProvider(String path, ZkClient zkClient) {
        return zkClient.delete(path);
    }
    
    /**
     * 获取指定Path数据
     * @param servicePath
     * @param zkClient
     * @return
     */
    public static Object getPathData(String servicePath, ZkClient zkClient) {
        return zkClient.readData(servicePath);
    }
}
