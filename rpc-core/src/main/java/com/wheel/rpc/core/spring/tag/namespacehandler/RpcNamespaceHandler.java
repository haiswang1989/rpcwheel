package com.wheel.rpc.core.spring.tag.namespacehandler;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.wheel.rpc.core.spring.tag.parser.ReferenceBeanDefinitionParser;
import com.wheel.rpc.core.spring.tag.parser.RegistryBeanDefinitionParser;
import com.wheel.rpc.core.spring.tag.parser.ServiceBeanDefinitionParser;

/**
 * RPC命名空间的标签的handler
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年5月31日 上午10:26:41
 */
public class RpcNamespaceHandler extends NamespaceHandlerSupport {
    
    private static final String ELEMENT_NAME_REFERENCE = "reference";
    private static final String ELEMENT_NAME_REGISTRY = "registry";
    private static final String ELEMENT_NAME_SERVICE = "service";
    
    
    @Override
    public void init() {
        registerBeanDefinitionParser(ELEMENT_NAME_REFERENCE, new ReferenceBeanDefinitionParser());
        registerBeanDefinitionParser(ELEMENT_NAME_REGISTRY, new RegistryBeanDefinitionParser());
        registerBeanDefinitionParser(ELEMENT_NAME_SERVICE, new ServiceBeanDefinitionParser());
    }
}
