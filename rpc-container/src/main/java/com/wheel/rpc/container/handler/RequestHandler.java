package com.wheel.rpc.container.handler;


import com.wheel.rpc.container.invoker.RequestInvoker;
import com.wheel.rpc.core.model.RpcRequest;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 请求处理
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午5:20:37
 */
public class RequestHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        RequestInvoker invoker = new RequestInvoker(rpcRequest);
        Object response = invoker.doInvoker();
        //往回写结果
        ctx.writeAndFlush(response);
    }
}
