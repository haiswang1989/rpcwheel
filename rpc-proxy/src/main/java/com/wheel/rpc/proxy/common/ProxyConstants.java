package com.wheel.rpc.proxy.common;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月25日 下午4:39:56
 */
public class ProxyConstants {
    
    /*proxy监听的端口*/
    public static final String PROXY_RPC_PORT = "proxy.rpc.port";
    
    /*proxy的worker线程数*/
    public static final String PROXY_RPC_NETTY_THREAD_WORKER = "proxy.rpc.netty.thread.worker";
    
    /*proxy的boss线程数*/
    public static final String PROXY_RPC_NETTY_THREAD_BOSS = "proxy.rpc.netty.thread.boss";
    
    /*proxy与server端的worker线程数*/
    public static final String PROXY2SERVER_RPC_NETTY_THREAD_WORKER = "proxy2server.rpc.netty.thread.worker";
}
