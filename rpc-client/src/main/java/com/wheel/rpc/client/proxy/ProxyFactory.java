package com.wheel.rpc.client.proxy;

import com.wheel.rpc.client.RpcClient;
import com.wheel.rpc.client.proxy.jdk.ClientInvocationHandler;
import com.wheel.rpc.client.proxy.jdk.ProxyGenerator;

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
     * @param rpcClient
     * @return
     */
    public static <T> T createProxy(Class<T> clazz, RpcClient rpcClient) {
        ClientInvocationHandler invocationHandler = new ClientInvocationHandler(clazz, rpcClient);
        ProxyGenerator<T> proxyGenerator = new ProxyGenerator<>(clazz, invocationHandler);
        return proxyGenerator.get();
    }
    
}
