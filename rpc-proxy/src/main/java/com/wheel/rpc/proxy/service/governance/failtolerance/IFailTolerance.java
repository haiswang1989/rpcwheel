package com.wheel.rpc.proxy.service.governance.failtolerance;

import com.wheel.rpc.core.model.RpcResponse;

/**
 * 服务的容错策略
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:56:32
 */
public interface IFailTolerance {
    
    public RpcResponse sendMessage(Object msg);
    
}
