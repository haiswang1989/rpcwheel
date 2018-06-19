package com.wheel.rpc.client.proxy.jdk;

import java.lang.reflect.Proxy;

/**
 * 代理生成器
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午2:42:46
 */
public class ProxyGenerator<T> {
    
    /** 需要代理的接口 */
    private Class<T> intfClass; 
    
    /**  */
    private ClientInvocationHandler invocationHandler;
    public ProxyGenerator() {
    }
    
    public ProxyGenerator(Class<T> intfClass, ClientInvocationHandler invocationHandler) {
        this.intfClass = intfClass;
        this.invocationHandler = invocationHandler;
    }
    
    @SuppressWarnings("unchecked")
    public T get() {
        Object proxy = Proxy.newProxyInstance(intfClass.getClassLoader(), 
                new Class<?>[]{intfClass}, invocationHandler);
        return (T)proxy;
    }
}
