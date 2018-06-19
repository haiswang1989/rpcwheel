package com.wheel.rpc.client.proxy;

import com.wheel.rpc.client.proxy.jdk.ClientInvocationHandler;
import com.wheel.rpc.client.proxy.jdk.ProxyGenerator;
import com.wheel.rpc.communication.channel.IRpcChannel;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午2:55:57
 */
public class ProxyFactory {
    
    /**
     * 
     * @param clazz
     * @param rpcChannel
     * @return
     */
    public static <T> T createProxy(Class<T> clazz, IRpcChannel rpcChannel) {
        ClientInvocationHandler invocationHandler = new ClientInvocationHandler(clazz, rpcChannel);
        ProxyGenerator<T> proxyGenerator = new ProxyGenerator<>(clazz, invocationHandler);
        return proxyGenerator.get();
    }
    
}
