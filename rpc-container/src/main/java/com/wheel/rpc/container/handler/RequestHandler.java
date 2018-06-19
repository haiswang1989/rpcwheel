package com.wheel.rpc.container.handler;

import java.lang.reflect.Method;

import com.wheel.rpc.container.common.ServerServiceRefCache;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;

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
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] methodType = rpcRequest.getParamsType();
        Object[] params = rpcRequest.getParamsValue();
        String requestId = rpcRequest.getRequestId();
        
        Class<?> clazz = Class.forName(serviceName);
        Method method = clazz.getMethod(methodName, methodType);
        
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        if(null == method) {
            System.err.println("serviceName : " + serviceName + ", call method : " + methodName + ", error : unknow method.");
            response.setE(null);
            response.setStatus(RpcStatus.ERROR);
        } else {
            Object ref = ServerServiceRefCache.getRef(serviceName);
            Object result = method.invoke(ref, params);
            response.setObj(result);
            response.setStatus(RpcStatus.SUCCESS);
        }
        
        //往回写结果
        ctx.writeAndFlush(response);
    }
}
