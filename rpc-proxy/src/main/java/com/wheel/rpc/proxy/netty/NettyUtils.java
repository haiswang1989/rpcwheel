package com.wheel.rpc.proxy.netty;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.handler.client.ProxyAsClientHandler;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 下午6:06:09
 */
public class NettyUtils {
    
    /**
     * 创建与provider的连接
     * @param serviceProviderNode
     * @param ioThreadCnt
     * @return
     */
    public static NettyRemotingClient createRemotingClient(ServiceProviderNode serviceProviderNode, int ioThreadCnt) {
        NettyRemotingClient proxyClient = new NettyRemotingClient(serviceProviderNode.getHostname(), serviceProviderNode.getPort(), ioThreadCnt);
        proxyClient.setChannelInitializer(new ProxyAsClientHandler());
        proxyClient.init();
        proxyClient.open();
        proxyClient.waitForDown();
        return proxyClient;
    }
    
}
