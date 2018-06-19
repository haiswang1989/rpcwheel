package com.wheel.rpc.core.spring.tag.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * service tag的解析类
 * 
 * interface 服务的接口
 * ref       接口实现类的实例
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 上午10:36:31
 */
public class ServiceBeanDefinitionParser extends AbstractRpcBeanDefinitionParser {
    
    private static final String ELEMENT_ID_INTERFACE = "interface";
    private static final String ELEMENT_ID_REF = "ref";
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String interfaceValue = getAttribute(element, ELEMENT_ID_INTERFACE, true);
        String refValue = getAttribute(element, ELEMENT_ID_REF, true);
        
        builder.addPropertyReference(ELEMENT_ID_INTERFACE, interfaceValue);
        builder.addPropertyReference(ELEMENT_ID_REF, refValue);
    }
    
}
