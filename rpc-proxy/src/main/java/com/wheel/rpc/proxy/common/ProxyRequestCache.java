package com.wheel.rpc.proxy.common;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * proxy端 请求id和response的hodler的映射
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午10:47:07
 */
public class ProxyRequestCache {
    
    /** 请求id 请求response的holder的映射 */
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
     * 设置请求的response
     * @param requestId
     */
    public static boolean setResponse(String requestId, RpcResponse response) {
        RpcResponseHolder rpcResponseHolder = RESPONSE_HOLDER.get(requestId);
        if(null == rpcResponseHolder) {
            return false;
        }
        
        rpcResponseHolder.set(response);
        //从缓存中删除responseholder
        RESPONSE_HOLDER.remove(requestId);
        return true;
    }
}

