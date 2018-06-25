package com.wheel.rpc.core.model;

import org.testng.annotations.Test;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 上午11:29:04
 */
public class RpcResponseHolderTest {
    
    @Test
    public void testGetTimeout() {
        
        RpcResponseHolder holder = new RpcResponseHolder();
        
        holder.get(5000l);
        
    }
}
