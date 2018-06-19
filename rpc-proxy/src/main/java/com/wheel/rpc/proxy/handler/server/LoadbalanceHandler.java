package com.wheel.rpc.proxy.handler.server;

import java.util.List;

import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.service.governance.loadbalance.ILoadbalance;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.RandomLoadbalance;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 
 * 负载均衡handler
 * 
 * 从Router输出的Provider中选取一个
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:21:41
 */
public class LoadbalanceHandler extends ChannelInboundHandlerAdapter {
    
    /** 负载均衡器 */ 
    private ILoadbalance loadbalance;
    
    public LoadbalanceHandler() {
        loadbalance = new RandomLoadbalance("");
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("LoadbalanceHandler." + loadbalance);
        RpcRequest request = (RpcRequest)msg;
        
        String serviceName = request.getServiceName();
        List<ServiceProviderNode> providerNodes = ProxyServiceCache.getServicesProviders(serviceName);
        request.setProvider(providerNodes.get(0));
        ctx.fireChannelRead(msg);
        
        //目标服务器
        /*
        ServiceProviderNode targetProvider = loadbalance.next();
        request.setProvider(targetProvider);
        ctx.write(request, promise);
        */
    }
}
