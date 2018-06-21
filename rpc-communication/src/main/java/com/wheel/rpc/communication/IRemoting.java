package com.wheel.rpc.communication;

/**
 * 远程调用的接口
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午9:20:22
 */
public interface IRemoting {
    
    /**
     * 打开
     */
    public Thread open();
    
    /**
     * 等待Open结束
     */
    public void waitForDown();
    
    /**
     * 关闭
     */
    public void close();
}
