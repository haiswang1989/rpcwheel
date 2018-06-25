package com.wheel.rpc.notify;

import java.util.concurrent.ConcurrentHashMap;

import com.wheel.rpc.common.RegistryConstants;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.notify.impl.zookeeper.ZookeeperNotify;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 上午10:56:54
 */
public class NotifyFactory {
    
    private static final ConcurrentHashMap<String, INotify> SERVICES_NOTIFY = new ConcurrentHashMap<>();
    
    /**
     * 
     * @param serviceName
     * @param registryConfigBean
     */
    public static INotify createNotify(String serviceName, RegistryConfigBean registryConfigBean) {
        
        INotify notify = SERVICES_NOTIFY.get(serviceName);
        if(null == notify) {
            String registryProtocol = registryConfigBean.getProtocol();
            if(RegistryConstants.PROTOCAL_ZOOKEEPER.equals(registryProtocol)) {
                return new ZookeeperNotify(serviceName);
            } else {
                return null;
            }
        }
        
        return notify;
    }
}
