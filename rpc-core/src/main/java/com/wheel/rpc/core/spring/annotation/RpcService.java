package com.wheel.rpc.core.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC服务的注解
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年5月31日 上午10:50:59
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {
    
}
