package com.wheel.rpc.proxy.handler.server;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;
import com.wheel.rpc.core.model.RpcStatus;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.service.governance.ILoadbalance;
import com.wheel.rpc.proxy.common.ProxyRequestCache;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.service.governance.loadbalance.impl.NormalRandomLoadbalance;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 请求转发,client的请求由Proxy转发到server
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午10:45:43
 */
public class RequestForwardHandler extends ChannelInboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(RequestForwardHandler.class);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest)msg;
        String serviceName = request.getServiceName();
        ServiceProviderNode providerNode = null;
        ILoadbalance loadbalance = null;
        if(CollectionUtils.isNotEmpty(request.getRouterNodes())) {
            //有路由的
            loadbalance = new NormalRandomLoadbalance(request.getRouterNodes(), serviceName);
        } else {
            //无路由的,优化过的
            loadbalance = request.getLoadbalance();
        }
        
        providerNode = loadbalance.next();
        
        RpcResponse rpcResponse = null;
        if(null == providerNode) {
            String errMsg = String.format("No online service provider, serviceName : %s", serviceName);
            LOG.error(errMsg);
            RpcException ex = new RpcException(errMsg);
            rpcResponse = new RpcResponse();
            rpcResponse.setE(ex);
            rpcResponse.setStatus(RpcStatus.ERROR);
            rpcResponse.setRequestId(request.getRequestId());
        } else {
            NettyRemotingClient nettyRemotingClient = ProxyServiceCache.getServiceTargetClient(request.getServiceName(), providerNode);
            if(null == nettyRemotingClient) {
                //TODO 这边可能获取不到指定的连接
                //服务的Provider刚刚下线,proxy端还没有感知
                //服务的Provider都下线了
                LOG.error("Target node is offline.");
                return ;
            }
            
            RpcResponseHolder rpcResponseHolder = ProxyRequestCache.setRequest(request);
            //通过proxy与server端的连接,将请求转发到server端
            nettyRemotingClient.getClientChannel().writeAndFlush(request);
            rpcResponse = rpcResponseHolder.get(1000l);
            if(null==rpcResponse) {
                String errMsg = String.format("Request timeout, serviceName : %s", serviceName);
                LOG.error(errMsg);
                RpcException ex = new RpcException(errMsg);
                rpcResponse = new RpcResponse();
                rpcResponse.setE(ex);
                rpcResponse.setStatus(RpcStatus.ERROR);
                rpcResponse.setRequestId(request.getRequestId());
            }
        }
        
        //拿到response以后,将response发送到client端
        ctx.writeAndFlush(rpcResponse);
    }
}

