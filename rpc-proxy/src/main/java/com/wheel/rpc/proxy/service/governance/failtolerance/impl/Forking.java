package com.wheel.rpc.proxy.service.governance.failtolerance.impl;

import com.wheel.rpc.core.model.RpcResponse;

/**
 * 并行调用
 * 
 * 同时调用多个服务器,有一个成功就返回
 * 
 * 用于实时性要求高的请求
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午3:50:20
 */
public class Forking extends AbstractFailTolerance {
    
    //并行度
    private int forkingParallelism;
    
    public Forking(int forkingParallelismArgs) {
        this.forkingParallelism = forkingParallelismArgs;
    }
    
    @Override
    public RpcResponse sendMessage(Object msg) {
        System.out.println(forkingParallelism);
        return null;
    }

}
