package com.wheel.rpc.core.model;

import java.net.InetSocketAddress;

import lombok.Data;

/**
 * 向注册中心注册服务时的Model 
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 下午2:18:48
 */
@Data
public class RegistryModel {
    
    /** 注册的服务接口 */
    @SuppressWarnings("rawtypes")
    private Class service;
    
    /** 服务暴露的IP地址与端口 */
    private InetSocketAddress address;
    
    /** server端服务治理相关的信息 */ 
    private ServiceGovernanceModel serviceGovernanceModel;
}
