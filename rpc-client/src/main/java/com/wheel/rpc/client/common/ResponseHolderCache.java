package com.wheel.rpc.client.common;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * response的hodler的缓存
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午3:03:49
 */
public class ResponseHolderCache {
    
    /** 请求的ID - 请求的response的holder */
    private static final ConcurrentHashMap<String, RpcResponseHolder> RPC_RESPONSE_HOLDERS = new ConcurrentHashMap<>();
    
    /**
     * 存放请求 和 请求对应的response的holder
     * @param requestId
     * @param responseHolder
     */
    public static void put(String requestId, RpcResponseHolder responseHolder) {
        RPC_RESPONSE_HOLDERS.put(requestId, responseHolder);
    }
    
    /**
     * 获取请求的response的holder
     * @param requestId
     */
    public static RpcResponseHolder get(String requestId) {
        return RPC_RESPONSE_HOLDERS.get(requestId);
    }
    
    /**
     * 删除请求的response的holder
     * @param requestId
     */
    public static void remove(String requestId) {
        RPC_RESPONSE_HOLDERS.remove(requestId);
    }
}
