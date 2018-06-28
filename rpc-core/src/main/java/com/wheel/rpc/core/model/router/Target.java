package com.wheel.rpc.core.model.router;

import lombok.Data;

/**
 * 请求的目标机器
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 上午10:34:26
 */
@Data
public class Target {
    
    /** 目标机器的IP*/
    private String addressIp;
    
    /** 目标机器的端口 */
    private int port;
}
