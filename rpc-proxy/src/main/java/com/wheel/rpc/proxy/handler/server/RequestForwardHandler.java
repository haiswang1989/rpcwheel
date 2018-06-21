package com.wheel.rpc.proxy.handler.server;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;
import com.wheel.rpc.proxy.common.ProxyRequestCache;
import com.wheel.rpc.proxy.common.ProxyServiceCache;

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
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest request = (RpcRequest)msg;
        NettyRemotingClient nettyRemotingClient = ProxyServiceCache.getNettyRemotingClient(request.getServiceName(), request.getProvider());
        if(null == nettyRemotingClient) {
            //TODO 这边可能获取不到指定的连接
            //服务的Provider刚刚下线,proxy端还没有感知
            //服务的Provider都下线了
        }
        
        RpcResponseHolder rpcResponseHolder = ProxyRequestCache.setRequest(request);
        //通过proxy与server端的连接,将请求转发到server端
        nettyRemotingClient.getClientChannel().writeAndFlush(request);
        RpcResponse rpcResponse = rpcResponseHolder.get();
        //拿到response以后,将response发送到client端
        ctx.writeAndFlush(rpcResponse);
    }
}

