package com.wheel.rpc.client.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.wheel.rpc.client.RpcClient;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;

/**
 * 将对代理的调用的方法的调用直接转换成RpcRequest对象
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午10:33:51
 */
public class ClientInvocationHandler implements InvocationHandler {
    
    private RpcClient rpcClient;
    
    private Class<?> clazz;
    
    public ClientInvocationHandler(Class<?> clazz, RpcClient rpcClient) {
        this.rpcClient = rpcClient;
        this.clazz = clazz;
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String className = clazz.getName();
        String methodName = method.getName();
        Class<?>[] methodParamsType = method.getParameterTypes();
        
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName(className);
        rpcRequest.setMethodName(methodName);
        rpcRequest.setParamsType(methodParamsType);
        rpcRequest.setParamsValue(args);
        
        RpcResponse rpcResponse = rpcClient.invoke(rpcRequest);
        RpcStatus status = rpcResponse.getStatus();
        if(RpcStatus.SUCCESS.equals(status)) {
            return rpcResponse.getObj();
        } else {
            throw rpcResponse.getE();
        }
    }
}
