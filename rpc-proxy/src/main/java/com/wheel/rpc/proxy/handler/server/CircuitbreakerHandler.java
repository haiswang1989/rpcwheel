package com.wheel.rpc.proxy.handler.server;

import java.util.ArrayList;
import java.util.List;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.model.circuitbreaker.CircuitBreakerModel;
import com.wheel.rpc.proxy.common.ProxyServiceCache;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 熔断的handler
 * 
 * 将熔断的服务器 从列表中剔除
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月29日 下午3:18:25
 */
public class CircuitbreakerHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        String serviceName = rpcRequest.getServiceName();
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        CircuitBreakerModel circuitBreakerModel = serviceGovernance.getCircuitBreakerModel();
        if(circuitBreakerModel.isCircuitBreakerEnabled()) {
            //开启了熔断
            //被熔断的Provider
            List<ServiceProviderNode> circuitbreakerProviders = ProxyServiceCache.getCircuitbreakerProviders(serviceName);
            if(circuitbreakerProviders.size() != 0) {
                //有被熔断的结点,LoadBalance就不能走优化了
                //优化的LoadBalance实现是以全部的在线Provider作为基础的,没有去除熔断的provider
                rpcRequest.setCanOptimize(false);
                List<ServiceProviderNode> allOnlineNodes = RegistryCache.getOnlineNodes(serviceName);
                //TODO 考虑优化,每次都Copy一份很伤
                List<ServiceProviderNode> allOnlineNodesCopy = new ArrayList<>(allOnlineNodes);
                //删除所有被熔断的结果
                allOnlineNodesCopy.removeAll(circuitbreakerProviders);
                rpcRequest.setAvailableProviders(allOnlineNodesCopy);
            }
        } 
        
        ctx.fireChannelRead(msg);
    }
}
