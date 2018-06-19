package com.wheel.rpc.core.spring.tag.parser;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.w3c.dom.Element;

/**
 * 默认的RPC Tag的bean的解析器
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月1日 上午10:38:20
 */
public class AbstractRpcBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    
    protected String getAttribute(Element element, String attributeName, boolean required) {
        String attributeValue = element.getAttribute(attributeName);
        if(required && StringUtils.isBlank(attributeValue)) {
            //TODO throw Exception
        } 
        
        return attributeValue;
    }
}
