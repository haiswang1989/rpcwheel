package com.wheel.rpc.proxy.service.governance.failtolerance.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.service.governance.ILoadbalance;
import com.wheel.rpc.proxy.common.ProxyRequestCache;
import com.wheel.rpc.proxy.common.ProxyServiceCache;

/**
 * 
 * 自动切换到其他的服务器
 *  
 * 需要配合尝试次数一起使用
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年7月6日 下午2:56:35
 */
public class FailOver extends AbstractFailTolerance {
    
    public static final Logger LOG = LoggerFactory.getLogger(FailOver.class);
    
    //尝试次数
    private int tryCount;
    
    public FailOver(int tryCountArgs) {
        this.tryCount = tryCountArgs;
    }
    
    @Override
    public RpcResponse sendMessage(Object msg) {
        RpcRequest rpcRequest = (RpcRequest)msg;
        ILoadbalance loadbalance = getLoadbalance(rpcRequest);
        RpcResponse rpcResponse = null;
        while(tryCount > 0) {
            ServiceProviderNode providerNode = loadbalance.next();
            if(null == providerNode) { //没有在线提供的服务
                LOG.error("Has no available node, serviceName : " + rpcRequest.getServiceName());
                break;
            }
            
            NettyRemotingClient nettyRemotingClient = ProxyServiceCache.getServiceTargetClient(rpcRequest.getServiceName(), providerNode);
            if(null == nettyRemotingClient) {
                LOG.error("Target node is offline, serviceName : " + rpcRequest.getServiceName() + ", targetNode : " + providerNode);
                continue;
            }
            
            RpcResponseHolder rpcResponseHolder = ProxyRequestCache.setRequest(rpcRequest);
            try {
                //通过proxy与server端的连接,将请求转发到server端
                nettyRemotingClient.getClientChannel().writeAndFlush(rpcRequest);
            } catch(Exception ex) {
                tryCount--;
                continue;
            }
            
            try {
                rpcResponse = rpcResponseHolder.get(1000l);
            } catch(Exception ex) {
                tryCount--;
                continue;
            }
            
            break;
        }
        
        return rpcResponse;
    }
}
