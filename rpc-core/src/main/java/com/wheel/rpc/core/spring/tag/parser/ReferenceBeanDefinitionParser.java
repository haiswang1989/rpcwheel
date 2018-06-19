package com.wheel.rpc.core.spring.tag.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * client端引用服务的标签 
 * 
 * id        spring bean的唯一ID
 * interface 服务的接口
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 上午11:12:54
 */
public class ReferenceBeanDefinitionParser extends AbstractRpcBeanDefinitionParser {
    
    private static final String ELEMENT_ID_ID = "id";
    private static final String ELEMENT_ID_INTERFACE = "interface";
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String interfaceValue = getAttribute(element, ELEMENT_ID_ID, true);
        String refValue = getAttribute(element, ELEMENT_ID_INTERFACE, true);
        
        builder.addPropertyReference(ELEMENT_ID_ID, interfaceValue);
        builder.addPropertyReference(ELEMENT_ID_INTERFACE, refValue);
    }
}
