package com.wheel.rpc.container.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午5:46:46
 */
public class ServerServiceRefCache {
    
    private static ConcurrentHashMap<String, Object> serverServiceRefMap = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param serviceName
     * @param obj
     */
    public static void put(String serviceName, Object obj) {
        serverServiceRefMap.put(serviceName, obj);
    }
    
    /**
     * 
     * @param serviceName
     * @return
     */
    public static Object getRef(String serviceName) {
        return serverServiceRefMap.get(serviceName);
    }
}
