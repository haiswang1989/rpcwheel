package com.wheel.rpc.core.spring.tag.parser;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.w3c.dom.Element;

/**
 * 配置中心的Tag的解析类
 * 
 * name 协议名称 如 zookeeper
 * address 地址如 127.0.0.1:2181
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 上午11:10:32
 */
public class RegistryBeanDefinitionParser extends AbstractRpcBeanDefinitionParser {
    
    private static final String ELEMENT_ID_NAME = "name";
    private static final String ELEMENT_ID_ADDRESS = "address";
    
    @Override
    protected void doParse(Element element, BeanDefinitionBuilder builder) {
        String interfaceValue = getAttribute(element, ELEMENT_ID_NAME, true);
        String refValue = getAttribute(element, ELEMENT_ID_ADDRESS, true);
        
        builder.addPropertyReference(ELEMENT_ID_NAME, interfaceValue);
        builder.addPropertyReference(ELEMENT_ID_ADDRESS, refValue);
    }
    
}
