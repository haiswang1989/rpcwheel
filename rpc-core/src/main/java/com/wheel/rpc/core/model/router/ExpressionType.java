package com.wheel.rpc.core.model.router;

/**
 * 表达式的类型
 * 
 * 
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 下午2:33:51
 */
public enum ExpressionType {
    
    STRING("String"), REGEXP("regexp"), RANGE("range"), NUMBER("number"), IP("ip"), PERCENT("percent");
    
    private String key; 
    
    private ExpressionType(String keyArgs) {
        this.key = keyArgs;
    }
    
    public String getKey() {
        return key;
    }
}
