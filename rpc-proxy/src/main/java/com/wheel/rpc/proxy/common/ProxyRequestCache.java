package com.wheel.rpc.proxy.common;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午10:47:07
 */
public class ProxyRequestCache {
    
    private static final ConcurrentHashMap<String, RpcResponseHolder> RESPONSE_HOLDER = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param request
     * @return
     */
    public static RpcResponseHolder setRequest(RpcRequest request) {
        RpcResponseHolder rpcResponseHolder = new RpcResponseHolder();
        RESPONSE_HOLDER.put(request.getRequestId(), rpcResponseHolder);
        return rpcResponseHolder;
    }
    
    /**
     * 
     * @param requestId
     */
    public static void setResponse(String requestId, RpcResponse response) {
        RpcResponseHolder rpcResponseHolder = RESPONSE_HOLDER.get(requestId);
        rpcResponseHolder.set(response);
        RESPONSE_HOLDER.remove(requestId);
    }
}

