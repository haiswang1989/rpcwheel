package com.wheel.rpc.registry;

import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.registry.impl.zookeeper.ZookeeperRegistry;

/**
 * Registry工厂
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月21日 上午10:16:53
 */
public class RegistryFactory {
    
    /** registry的协议 zookeeper */
    private static String PROTOCAL_ZOOKEEPER = "zookeeper";
    
    /**
     * 
     * @param registryConfigBean
     * @return
     */
    public static IRegistry createRegistry(RegistryConfigBean registryConfigBean) {
        String registryProtocol = registryConfigBean.getProtocol();
        if(PROTOCAL_ZOOKEEPER.equals(registryProtocol)) {
            return ZookeeperRegistry.getInstance(registryConfigBean);
        } else {
            return null;
        }
    }
}
