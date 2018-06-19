package com.wheel.rpc.serialization;

/**
 * 序列化接口
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月6日 上午11:52:52
 */
public interface ISerializer {
    
    /**
     * 
     * @param obj
     * @return
     */
    public byte[] serialize(Object obj);
    
    /**
     * 
     * @param bytes
     * @param clazz
     * @return
     */
    public <T> T deSerialize(byte[] bytes,  Class<T> clazz);
}
