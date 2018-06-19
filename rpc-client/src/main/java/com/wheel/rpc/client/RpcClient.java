package com.wheel.rpc.client;

import com.wheel.rpc.client.common.ClientResponseCache;
import com.wheel.rpc.client.handler.ClientHandler;
import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcResponseHolder;

import io.netty.channel.ChannelOption;

/**
 * rpc客户端
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午2:37:39
 */
public class RpcClient {
    
    private NettyRemotingClient client;
    
    private ClientHandler clientHandler;
    
    /** proxy的IP */ 
    private String ip;
    
    /** proxy的端口 */
    private int port;
    
    /** IO操作的线程数 */
    private int ioThreadCnt;
    
    public RpcClient(String ip, int port) {
        this(ip, port, 0);
    }
    
    public RpcClient(String ip, int port, int ioThreadCnt) {
        this.ip = ip;
        this.port = port;
        this.ioThreadCnt = ioThreadCnt;
    }
    
    public void open() {
        if(0 == ioThreadCnt) {
            client = new NettyRemotingClient(ip, port);
        } else {
            client = new NettyRemotingClient(ip, port, ioThreadCnt);
        }
        
        clientHandler = new ClientHandler();
        client.handler(clientHandler).option(ChannelOption.TCP_NODELAY, true);
        client.open();
    }
    
    /**
     * 
     * @param rpcRequest
     */
    public RpcResponse invoke(RpcRequest rpcRequest) {
        String requestId = rpcRequest.getRequestId();
        RpcResponseHolder rpcResponseHolder = new RpcResponseHolder();
        ClientResponseCache.put(requestId, rpcResponseHolder);
        client.getClientChannel().writeAndFlush(rpcRequest);
        return rpcResponseHolder.get();
    }
}
