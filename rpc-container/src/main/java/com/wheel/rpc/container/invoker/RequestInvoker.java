package com.wheel.rpc.container.invoker;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.wheel.rpc.container.common.ClazzCache;
import com.wheel.rpc.container.common.ServicesRefCache;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午6:06:05
 */
public class RequestInvoker {
    
    private RpcRequest rpcRequest;
    
    private Method method;
    private Object ref;
    
    public RequestInvoker(RpcRequest rpcRequestArgs) {
        this.rpcRequest = rpcRequestArgs;
    }
    
    /**
     * 
     */
    private void preInvoker() {
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] methodParamsType = rpcRequest.getParamsType();
        
        Class<?> clazz = ClazzCache.getCreateIfNotExist(serviceName);
        if(null == clazz) {
            throw new RpcException(String.format("Class not found , class name : {}", clazz));
        }
        
        try {
            //TODO move到cache中,每次反射调用耗时
            method = clazz.getMethod(methodName, methodParamsType);
        } catch (NoSuchMethodException e) {
            throw new RpcException(e);
        } catch (SecurityException e) {
            throw new RpcException(e);
        }
        
        ref = ServicesRefCache.getRef(serviceName);
        if(null == ref) {
            throw new RpcException(String.format("Ref not found for class : {}", clazz));
        }
    }
    
    /**
     * 
     * @return
     */
    public Object doInvoker() {
        preInvoker();
        Object[] params = rpcRequest.getParamsValue();
        String requestId = rpcRequest.getRequestId();
        
        Object result = null; 
        RpcException ex = null;
        RpcStatus status = RpcStatus.SUCCESS;
        try {
            result = method.invoke(ref, params);
        } catch (IllegalAccessException e) {
            ex = new RpcException(e);
            status = RpcStatus.ERROR;
        } catch (IllegalArgumentException e) {
            ex = new RpcException(e);
            status = RpcStatus.ERROR;
        } catch (InvocationTargetException e) {
            ex = new RpcException(e);
            status = RpcStatus.ERROR;
        }
        
        RpcResponse response = new RpcResponse();
        response.setRequestId(requestId);
        response.setObj(result);
        response.setStatus(status);
        response.setE(ex);
        return response;
    }
    
}
