package com.wheel.rpc.core.config.bean;

import org.springframework.beans.factory.InitializingBean;
import lombok.Data;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午9:48:46
 */
@Data
public class ReferenceConfigBean<T> implements InitializingBean {
    
    /** 服务的接口 */
    private Class<T> interfaceClazz;
    
    /** 获取接口代理实例的spring的beanname */
    private String refBeanName;
    
    /** 接口代理的实例 */
    private T ref;
    
    public ReferenceConfigBean(Class<T> interfaceClazz, T ref) {
        this.interfaceClazz = interfaceClazz;
        this.ref = ref;
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
    }
    
}
