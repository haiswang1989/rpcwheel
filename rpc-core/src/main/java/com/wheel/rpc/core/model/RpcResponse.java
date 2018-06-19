package com.wheel.rpc.core.model;

import lombok.Data;

/**
 * Rpc请求的返回值
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午2:29:10
 */
@Data
public class RpcResponse {
    
    /** 请求的Id */
    private String requestId;
    
    /** 运行结果的状态 */
    private RpcStatus status;
    
    /** 返回的结果 */
    private Object obj;
    
    /** 执行异常 */
    private Exception e;
}
