package com.wheel.rpc.client.invoke;

import com.wheel.rpc.client.common.ClientResponseCache;
import com.wheel.rpc.communication.channel.IRpcWriteChannel;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午2:47:49
 */
public class RequestProcesser {
    
    private IRpcWriteChannel rpcWriteChannel;
    
    private RpcRequest rpcRequest;
    
    public RequestProcesser(IRpcWriteChannel rpcWriteChannelArgs, RpcRequest rpcRequestArgs) {
        this.rpcWriteChannel = rpcWriteChannelArgs;
        this.rpcRequest = rpcRequestArgs;
    }
    
    public RpcResponse doInvoke() {
        String requestId = rpcRequest.getRequestId();
        RpcResponseHolder rpcResponseHolder = new RpcResponseHolder();
        ClientResponseCache.put(requestId, rpcResponseHolder);
        rpcWriteChannel.writeAndFlush(rpcRequest);
        return rpcResponseHolder.get();
    }
}
