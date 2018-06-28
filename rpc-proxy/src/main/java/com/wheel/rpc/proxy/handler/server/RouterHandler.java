package com.wheel.rpc.proxy.handler.server;

import java.util.List;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.model.router.Routes;
import com.wheel.rpc.proxy.router.Router;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 路由的handler
 * 
 * 从所有的Provider中选取部分Provider
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午5:20:24
 */
public class RouterHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        String serviceName = rpcRequest.getServiceName();
        
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        Routes routes = serviceGovernance.getRoutes();
        if(routes.isOpen()) {
            //路由打开了
            Router router = new Router(routes, rpcRequest);
            List<ServiceProviderNode> providers = router.doRouter();
            rpcRequest.setRouterNodes(providers);
        } 
        
        ctx.fireChannelRead(msg);
    }
}
