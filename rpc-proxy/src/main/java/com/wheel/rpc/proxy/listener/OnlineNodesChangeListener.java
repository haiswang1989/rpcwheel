package com.wheel.rpc.proxy.listener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.config.listener.impl.AbstractZkConfigChangeListener;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.common.ProxyConstants;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.netty.NettyUtils;

/**
 * 结点上下线变化的处理
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 下午2:55:19
 */
public class OnlineNodesChangeListener extends AbstractZkConfigChangeListener {
    
    private String serviceName;
    
    public OnlineNodesChangeListener(String serviceNameArgs) {
        this.serviceName = serviceNameArgs;
    }
    
    @Override
    public void onlineNodesChange(List<ServiceProviderNode> onlineNodes, List<ServiceProviderNode> offlineNodes) {
        ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> allRemotingClients = ProxyServiceCache.serviceClients(serviceName);
        for (ServiceProviderNode offlineNode : offlineNodes) {
            NettyRemotingClient remotingClient = allRemotingClients.get(offlineNode);
            allRemotingClients.remove(offlineNode);
            //关闭连接
            remotingClient.close();
        }
        
        //创建新的连接
        String workCount = System.getProperty(ProxyConstants.PROXY2SERVER_RPC_NETTY_THREAD_WORKER);
        int workerCnt = Integer.parseInt(workCount);
        for (ServiceProviderNode onlineNode : onlineNodes) {
            NettyRemotingClient client = NettyUtils.createRemotingClient(onlineNode, workerCnt);
            allRemotingClients.put(onlineNode, client);
        }
    }
}
