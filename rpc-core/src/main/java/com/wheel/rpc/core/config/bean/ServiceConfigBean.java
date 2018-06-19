package com.wheel.rpc.core.config.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Data;

/**
 * RPC服务的配置
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午9:45:24
 */
@Data
public class ServiceConfigBean<T> implements InitializingBean, ApplicationContextAware {
    
    /** 服务的接口 */
    private Class<T> interfaceClazz;
    
    /** 接口实现类的实例 */
    private String refBeanName; 
    private T ref;
    
    ApplicationContext applicationContext;
    
    public ServiceConfigBean(Class<T> interfaceClazz, T ref) {
        this.interfaceClazz = interfaceClazz;
        this.ref = ref;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化接口的实现类的实例
        ref = applicationContext.getBean(refBeanName, interfaceClazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
