package com.wheel.rpc.container.common;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类的名称和其Class对象的映射
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午6:11:44
 */
public class ClazzCache {
    
    public static final Logger LOG = LoggerFactory.getLogger(ClazzCache.class);
    
    /** 类名称 Class对象的映射 */
    private static final ConcurrentHashMap<String, Class<?>> CLASSES_CACHE = new ConcurrentHashMap<>();
    
    /**
     * 获取类的class对象,没有就创建
     * @param className
     * @return
     */
    public static Class<?> getCreateIfNotExist(String className) {
        Class<?> clazz = CLASSES_CACHE.get(className);
        if(null == clazz) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                LOG.error("", e);
                return null;
            }
            
            CLASSES_CACHE.put(className, clazz);
        }
        
        return clazz;
    }
}
