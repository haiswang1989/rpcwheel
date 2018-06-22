package com.wheel.rpc.proxy.handler.server;

import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.service.governance.loadbalance.ILoadbalance;

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
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest)msg;
        String serviceName = request.getServiceName();
        ILoadbalance loadbalance = ProxyServiceCache.servicesLoadbalanceStrategy(serviceName);
        request.setProvider(loadbalance.next());
        ctx.fireChannelRead(msg);
    }
}
