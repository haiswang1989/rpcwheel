package com.wheel.rpc.core.spring.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC方法的注解
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年5月31日 上午10:51:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcMethod {
    
}
