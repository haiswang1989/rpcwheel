package com.wheel.rpc.core.config.bean;

import lombok.Data;

/**
 * 注册中心的相关信息
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午9:42:58
 */
@Data
public class RegistryConfigBean {
    
    /**
     * 注册中心使用的协议
     * 如:zookeeper 
     */
    private String protocol;
    
    /**
     * 注册中心的连接串
     * 如：127.0.0.1:2181,127.0.0.2:2181
     */
    private String connection;
    
}
