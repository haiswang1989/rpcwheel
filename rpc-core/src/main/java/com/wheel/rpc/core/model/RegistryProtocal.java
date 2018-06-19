package com.wheel.rpc.core.model;

import lombok.Getter;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 下午5:14:07
 */
public enum RegistryProtocal {
    
    ZOOKEEPER("zookeeper");
    
    @Getter
    private String value;
    
    RegistryProtocal(String value) {
        this.value = value;
    }
    
    public boolean is(String protocal) {
        return this.value.equalsIgnoreCase(protocal);
    }
}
