package com.wheel.rpc.core.model;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午2:30:57
 */
public class RpcResponseHolder {
    
    private volatile RpcResponse rpcResponse;
    
    private Object obj;
    
    public RpcResponseHolder() {
        obj = new Object();
    }
    
    public RpcResponse get(long timeout) {
        if(null == rpcResponse) {
            synchronized (obj) {
                while(true) {
                    if(null==rpcResponse) {
                        try {
                            obj.wait(timeout);
                        } catch (InterruptedException e) {
                            continue;
                        }
                        break;
                    } else {
                        break;
                    }
                }
            }
        }
        
        return rpcResponse;
    }
    
    /**
     * 
     * @param response
     */
    public void set(RpcResponse response) {
        synchronized (obj) {
            this.rpcResponse = response;
            obj.notifyAll();
        }
    }
}
