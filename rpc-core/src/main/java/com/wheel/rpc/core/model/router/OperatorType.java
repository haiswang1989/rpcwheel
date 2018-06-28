package com.wheel.rpc.core.model.router;

/**
 * 关系运算符类型
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 下午2:38:02
 */
public enum OperatorType {
    
    AND("and"), OR("or");
    
    private String operator;
    
    private OperatorType(String operatorArgs) {
        this.operator = operatorArgs;
    }
    
    public String getOperator() {
        return operator;
    }
}
