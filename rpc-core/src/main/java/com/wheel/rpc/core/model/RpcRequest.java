package com.wheel.rpc.core.model;

import java.util.List;
import java.util.UUID;

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
    
    /** 请求的Id */
    public String requestId; 
    
    /** 服务的名称 */
    private String serviceName;
    
    /** 方法的名称 */
    private String methodName;
    
    /** 方法参数类型 */
    private Class<?>[] paramsType;
    
    /** 方法的各参数的值 */
    private Object[] paramsValue;
    
    /** 所有的服务的提供者 */
    private List<ServiceProviderNode> allNodes;
    
    /** 经过路由之后,可选的提供者 */
    private List<ServiceProviderNode> afterRouterNodes;
    
    /** 经过Loadbanlance选择后,最终请求的服务结点 */
    private ServiceProviderNode provider;
    
    /**
     * 裁剪请求参数,减小带宽
     */
    public void cut() {
        allNodes = null;
        afterRouterNodes = null;
        provider = null;
    }
}
