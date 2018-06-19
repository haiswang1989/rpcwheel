package com.wheel.rpc.core.model;

import com.wheel.rpc.core.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 服务的提供结点
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午4:28:06
 */
@Data
@AllArgsConstructor
@EqualsAndHashCode
public class ServiceProviderNode {
    
    public ServiceProviderNode(String ipPort) {
        String[] ipPortAry = ipPort.split(Constants.IP_PORT_CONNECTOR_CHAR);
        hostname = ipPortAry[0];
        port = Integer.parseInt(ipPortAry[1]);
    }
    
    /** ip */
    private String hostname;
    
    /** 端口 */
    private int port;
}
