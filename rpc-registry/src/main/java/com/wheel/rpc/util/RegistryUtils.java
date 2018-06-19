package com.wheel.rpc.util;

import com.wheel.rpc.core.common.Constants;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月12日 下午3:35:32
 */
public class RegistryUtils {
    
    /**
     * 通过ZK的路径,获取服务的名称
     * /${}/${}/${serviceName}
     * 
     * @param dataPath
     * @return
     */
    public static String getServiceName(String dataPath) {
        return dataPath.substring(dataPath.lastIndexOf(Constants.PATH_SEPARATOR));
    }
}
