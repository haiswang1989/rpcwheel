package com.wheel.rpc.proxy.service.governance.failtolerance.impl;

import com.wheel.rpc.core.model.RpcResponse;

/**
 * 快速失败
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午3:43:48
 */
public class FailFast extends AbstractFailTolerance {

    @Override
    public RpcResponse sendMessage(Object msg) {
        return null;
    }

}
