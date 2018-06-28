package com.wheel.rpc.core.model;

import java.util.List;
import java.util.UUID;

import com.wheel.rpc.core.service.governance.ILoadbalance;

import lombok.Data;

/**
 * RPC 请求
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午11:23:52
 */
@Data
public class RpcRequest {
    
    public RpcRequest() {
        requestId = UUID.randomUUID().toString();
    }
    
    /** 请求client端的ID */
    private String callerId;
    
    /** 请求client端的IP */
    private String callerIp;
    
    /** 请求的Id */
    private String requestId; 
    
    /** 服务的名称 */
    private String serviceName;
    
    /** 方法的名称 */
    private String methodName;
    
    /** 方法参数类型 */
    private Class<?>[] paramsType;
    
    /** 方法的各参数的值 */
    private Object[] paramsValue;
    
    /** 经过路由之后的提供者 */
    private List<ServiceProviderNode> routerNodes;
    
    /** 负载均衡的实现 */
    private ILoadbalance loadbalance;
    
    /**
     * 裁剪请求参数,减小带宽
     */
    public void cut() {
    }
}
