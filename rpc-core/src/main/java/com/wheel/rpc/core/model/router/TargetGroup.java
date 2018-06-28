package com.wheel.rpc.core.model.router;

import lombok.Data;

/**
 * 一组目标机器
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 上午10:36:09
 */
@Data
public class TargetGroup {
    
    /** 组的名称 */
    private String name;
    
    /** 目标机器的集合 */
    private Target[] targets;
}
