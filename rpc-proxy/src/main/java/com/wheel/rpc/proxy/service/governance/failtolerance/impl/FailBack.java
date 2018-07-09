package com.wheel.rpc.proxy.service.governance.failtolerance.impl;

import com.wheel.rpc.core.model.RpcResponse;

/**
 * 自动恢复
 * 
 * 失败以后在后台记录,后台定时发送
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午3:49:29
 */
public class FailBack extends AbstractFailTolerance {

    @Override
    public RpcResponse sendMessage(Object msg) {
        return null;
    }

}
