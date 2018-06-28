package com.wheel.rpc.core.model.router;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * 规则的集合
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月28日 上午9:52:36
 */
@Data
public class Routes {
    
    /** 是否开启 */
    private boolean isOpen = false;
    
    /** 一组规则 */
    private List<Route> routes;
    
    /** TargetGroup的名称 和  TargetGroup的映射*/
    private Map<String, TargetGroup> name2TargetGroup;
    
    /**
     * 
     * @param targetGroupName
     * @return
     */
    public TargetGroup getTargetGroup(String targetGroupName) {
        return name2TargetGroup.get(targetGroupName);
    }
}
