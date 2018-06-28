package com.wheel.rpc.container.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.exception.DegradationException;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;
import com.wheel.rpc.core.model.ServiceGovernanceModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端的"降级"处理
 * 
 * 人工降级,由服务方统一降级,至于客户端的降级处理,需要客户端自己捕获异常
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:18:17
 */
public class DegradationHandler extends ChannelInboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(DegradationHandler.class);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        String serviceName = rpcRequest.getServiceName();
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        boolean isDegradation = serviceGovernance.isDegradation();
        if(isDegradation) {
            String msgInfo = String.format("Service is degradation, serviceName : %s", serviceName);
            LOG.info(msgInfo);
            //服务被降级了
            DegradationException degradationException = new DegradationException(msgInfo);
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.setE(degradationException);
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setStatus(RpcStatus.ERROR);
            ctx.writeAndFlush(rpcResponse);
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
