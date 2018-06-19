package com.wheel.rpc.client;

import com.wheel.rpc.client.handler.ClientHandler;
import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;

import io.netty.channel.Channel;

/**
 * rpc客户端
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午2:37:39
 */
public class RpcClient {
    
    private NettyRemotingClient client;
    
    /** proxy的IP */ 
    private String ip;
    
    /** proxy的端口 */
    private int port;
    
    /** IO操作的线程数 */
    private int ioThreadCnt;
    
    public RpcClient(String ipArgs, int portArgs, int ioThreadCntArgs) {
        this.ip = ipArgs;
        this.port = portArgs;
        this.ioThreadCnt = ioThreadCntArgs;
    }
    
    public void open() {
        client = new NettyRemotingClient(ip, port, ioThreadCnt);
        client.setChannelInitializer(new ClientHandler());
        client.init();
        client.open();
    }
    
    /**
     * 
     * @return
     */
    public Channel getChannel() {
        return client.getClientChannel();
    }
}
