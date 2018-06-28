package com.wheel.rpc.core.model.router;

import lombok.Data;

/**
 * 路由
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 下午2:45:45
 */
@Data
public class Route {
    
    /** 规则 */
    private Rule rule;
    
    /**  */
    private Action action;
    
}
