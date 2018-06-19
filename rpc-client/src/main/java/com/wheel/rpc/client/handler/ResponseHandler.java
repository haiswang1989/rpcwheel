package com.wheel.rpc.client.handler;

import com.wheel.rpc.client.common.ClientResponseCache;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 对于Proxy返回的结果进行处理
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午3:12:06
 */
public class ResponseHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcResponse rpcResponse = (RpcResponse)msg;
        RpcResponseHolder responseHolder = ClientResponseCache.get(rpcResponse.getRequestId());
        //把对应的RequestId的Response
        responseHolder.set(rpcResponse);
        //删除映射
        ClientResponseCache.remove(rpcResponse.getRequestId());
    }
}
