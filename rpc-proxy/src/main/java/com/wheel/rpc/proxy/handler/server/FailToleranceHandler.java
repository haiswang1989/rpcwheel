package com.wheel.rpc.proxy.handler.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.FailTolerance;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.proxy.service.governance.failtolerance.FailToleranceFactory;
import com.wheel.rpc.proxy.service.governance.failtolerance.IFailTolerance;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 容错策略的handler
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午2:58:31
 */
public class FailToleranceHandler extends ChannelInboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(FailToleranceHandler.class);
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        String serviceName = rpcRequest.getServiceName();
        //服务治理模型
        ServiceGovernanceModel serviceGovernance = RegistryCache.getServiceGovernance(serviceName);
        //容错策略类型
        FailTolerance failToleranceStrategyType = serviceGovernance.getFailToleranceStrategyType();
        //容错策略
        IFailTolerance failToleranceStrategy = FailToleranceFactory.createFailTolerance(failToleranceStrategyType, serviceGovernance);
        RpcResponse rpcResponse = failToleranceStrategy.sendMessage(msg);
        if(null==rpcResponse) {
            String errMsg = String.format("Get request response timeout, serviceName : %s", serviceName);
            LOG.error(errMsg);
            RpcException ex = new RpcException(errMsg);
            rpcResponse = new RpcResponse();
            rpcResponse.setE(ex);
            rpcResponse.setStatus(RpcStatus.ERROR);
            rpcResponse.setRequestId(rpcRequest.getRequestId());
        }
        
        //拿到response以后,将response发送到client端
        ctx.writeAndFlush(rpcResponse);
    }
}
