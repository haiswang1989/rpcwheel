package com.wheel.rpc.client.common;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.core.model.RpcResponseHolder;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午3:03:49
 */
public class ClientResponseCache {
    
    private static ConcurrentHashMap<String, RpcResponseHolder> rpcResponseHolders = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param requestId
     * @param responseHolder
     */
    public static void put(String requestId, RpcResponseHolder responseHolder) {
        rpcResponseHolders.put(requestId, responseHolder);
    }
    
    /**
     * 
     * @param requestId
     */
    public static RpcResponseHolder get(String requestId) {
        return rpcResponseHolders.get(requestId);
    }
    
    /**
     * 
     * @param requestId
     */
    public static void remove(String requestId) {
        rpcResponseHolders.remove(requestId);
    }
}
