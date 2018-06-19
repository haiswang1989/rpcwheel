package com.wheel.rpc.client.invoke;

import com.wheel.rpc.client.common.ClientResponseCache;
import com.wheel.rpc.communication.channel.IRpcChannel;
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
    
    private IRpcChannel rpcChannel;
    
    private RpcRequest rpcRequest;
    
    public RequestProcesser(IRpcChannel rpcChannelArgs, RpcRequest rpcRequestArgs) {
        this.rpcChannel = rpcChannelArgs;
        this.rpcRequest = rpcRequestArgs;
    }
    
    public RpcResponse doInvoke() {
        String requestId = rpcRequest.getRequestId();
        RpcResponseHolder rpcResponseHolder = new RpcResponseHolder();
        ClientResponseCache.put(requestId, rpcResponseHolder);
        rpcChannel.writeAndFlush(rpcRequest);
        return rpcResponseHolder.get();
    }
}
