package com.wheel.rpc.client.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.client.common.ClientConstants;
import com.wheel.rpc.client.invoke.RequestProcesser;
import com.wheel.rpc.communication.channel.IRpcWriteChannel;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.exception.RpcException;
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
    
    public static final Logger LOG = LoggerFactory.getLogger(ClientInvocationHandler.class);
    
    private IRpcWriteChannel rpcWriteChannel;
    
    private Class<?> clazz;
    
    public ClientInvocationHandler(Class<?> clazzArgs, IRpcWriteChannel rpcWriteChannelArgs) {
        this.rpcWriteChannel = rpcWriteChannelArgs;
        this.clazz = clazzArgs;
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
        rpcRequest.setCallerId(System.getProperty(ClientConstants.CLIENT_RPC_CALLER_ID));
        rpcRequest.setCallerIp(CommonUtils.getLocalAddressIp());
        RequestProcesser processer = new RequestProcesser(rpcWriteChannel, rpcRequest);
        RpcResponse rpcResponse = processer.doInvoke();
        if(null == rpcResponse) {
            String errMsg = String.format("Call proxy timeout ,serviceName %s", className);
            LOG.error(errMsg);
            throw new RpcException(errMsg);
        }
        
        RpcStatus status = rpcResponse.getStatus();
        if(RpcStatus.SUCCESS.equals(status)) {
            return rpcResponse.getObj();
        } else {
            throw rpcResponse.getE();
        }
    }
}
