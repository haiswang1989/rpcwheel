package com.wheel.rpc.container.common;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 下午4:45:14
 */
public class ServerConstants {
    
    /*server端监听的端口*/
    public static final String SERVER_RPC_PORT = "server.rpc.port";
    
    /*server端的worker线程数*/
    public static final String SERVER_RPC_NETTY_THREAD_WORKER = "server.rpc.netty.thread.worker";
    
    /*server端的boss线程数*/
    public static final String SERVER_RPC_NETTY_THREAD_BOSS = "server.rpc.netty.thread.boss";
}
