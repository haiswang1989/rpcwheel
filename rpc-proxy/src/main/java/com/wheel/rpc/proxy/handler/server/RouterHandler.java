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
            //开启了路由就不走LoadBalance就不能走优化了
            rpcRequest.setCanOptimize(false);
            //路由打开了
            Router router = new Router(routes, rpcRequest);
            //路由有效结点
            List<ServiceProviderNode> routerRroviders = router.doRouter();
            //有效结点的基准
            List<ServiceProviderNode> baseProviders;
            //是否有熔断
            if(serviceGovernance.getCircuitBreakerModel().isCircuitBreakerEnabled()) {
                //如果开启了熔断,就以熔断后的结果为基准
                baseProviders = rpcRequest.getAvailableProviders();
            } else {
                //如果没有开启熔断,那么就以注册中心的结果为基准
                baseProviders = RegistryCache.getOnlineNodes(serviceName);
            }
            
            baseProviders.retainAll(routerRroviders);
            rpcRequest.setAvailableProviders(baseProviders);
        } 
        
        ctx.fireChannelRead(msg);
    }
}
