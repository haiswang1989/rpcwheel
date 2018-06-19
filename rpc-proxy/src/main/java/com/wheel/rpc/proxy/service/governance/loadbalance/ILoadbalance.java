package com.wheel.rpc.proxy.service.governance.loadbalance;

import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 
 * 负载均衡接口
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:24:46
 */
public interface ILoadbalance {
    
    /**
     * 获取调用结点
     * @return
     */
    public ServiceProviderNode next();
    
    /**
     * 刷新
     */
    public void refresh();
    
}
