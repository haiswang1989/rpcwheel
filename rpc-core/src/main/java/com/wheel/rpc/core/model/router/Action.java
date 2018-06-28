package com.wheel.rpc.core.model.router;

import lombok.Data;

/**
 * 动作,行为
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 上午10:38:21
 */
@Data
public class Action {
    
    /** action类型,取值"forward" , "reject" */
    private String type;
    
    /** 请求的机组目标机器 */
    private String[] targetGroupNames;
}
