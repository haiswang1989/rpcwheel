package com.wheel.rpc.client.invoke;

import com.wheel.rpc.client.common.ResponseHolderCache;
import com.wheel.rpc.communication.channel.IRpcWriteChannel;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * request请求的处理器
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午2:47:49
 */
public class RequestProcesser {
    
    /** 写channel */
    private IRpcWriteChannel rpcWriteChannel;
    
    /** 请求 */
    private RpcRequest rpcRequest;
    
    public RequestProcesser(IRpcWriteChannel rpcWriteChannelArgs, RpcRequest rpcRequestArgs) {
        this.rpcWriteChannel = rpcWriteChannelArgs;
        this.rpcRequest = rpcRequestArgs;
    }
    
    public RpcResponse doInvoke() {
        String requestId = rpcRequest.getRequestId();
        RpcResponseHolder rpcResponseHolder = new RpcResponseHolder();
        ResponseHolderCache.put(requestId, rpcResponseHolder);
        rpcWriteChannel.writeAndFlush(rpcRequest);
        return rpcResponseHolder.get();
    }
}
