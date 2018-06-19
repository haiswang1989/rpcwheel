package com.wheel.rpc.communication.client.impl.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.client.impl.AbstractRemotingClient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;

/**
 * 
 * 基于Netty的client端
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午9:22:58
 */
public class NettyRemotingClient extends AbstractRemotingClient {
    
    public static final Logger LOG = LoggerFactory.getLogger(NettyRemotingClient.class);
    
    private static final int THREAD_POOL_SIZE = 5;
    
    private String ip;
    private int port;
    private int ioThreadCnt;
    
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    
    private ChannelFuture channelFuture;
    
    @Getter
    private Channel clientChannel;
    
    public NettyRemotingClient(String ip, int port) {
        this(ip, port, THREAD_POOL_SIZE);
    }
    
    public NettyRemotingClient(String ip, int port, int ioThreadCnt) {
        this.ip = ip;
        this.port = port;
        this.ioThreadCnt = ioThreadCnt;
        
        group = new NioEventLoopGroup(this.ioThreadCnt);
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
    }
    
    /**
     * 
     * @param option
     * @param value
     * @return
     */
    public <T> NettyRemotingClient option(ChannelOption<T> option, T value) {
        bootstrap.option(option, value);
        return this;
    }
    
    /**
     * 
     * @param handler
     * @return
     */
    public NettyRemotingClient handler(ChannelHandler handler) {
        bootstrap.handler(handler);
        return this;
    }
    
    @Override
    public void open() {
        try {
            channelFuture = bootstrap.connect(ip, port).sync();
        } catch (InterruptedException e) {
            LOG.error("Connect to server failed.", e);
            System.exit(-1);
        }
        
        System.out.println("Connect to service success.");
        
        clientChannel = channelFuture.channel();
        
        try {
            clientChannel.closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Client wait for close interrupted.", e);
        }
        
        System.out.println("Connection is break.");
    }

    @Override
    public void close() {
        group.shutdownGracefully();
        clientChannel.close();
    }
}
