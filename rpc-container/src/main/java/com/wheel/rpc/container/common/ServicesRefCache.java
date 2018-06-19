package com.wheel.rpc.container.common;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务实现类的实例的缓存
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午5:46:46
 */
public class ServicesRefCache {
    
    /** 服务与服务实现类的对象的映射,用于反射调用 */
    private static final ConcurrentHashMap<String, Object> SERVICES_REF = new ConcurrentHashMap<>();
    
    /**
     * 存放service的实现类的实例
     * @param serviceName
     * @param obj
     */
    public static void put(String serviceName, Object obj) {
        SERVICES_REF.put(serviceName, obj);
    }
    
    /**
     * 获取service的实现类的实例
     * @param serviceName
     * @return
     */
    public static Object getRef(String serviceName) {
        return SERVICES_REF.get(serviceName);
    }
}
