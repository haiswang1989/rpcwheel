package com.wheel.rpc.communication.client.impl.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.client.impl.AbstractRemotingClient;
import com.wheel.rpc.core.exception.RpcException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Getter;
import lombok.Setter;

/**
 * 基于Netty的client端
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午9:22:58
 */
public class NettyRemotingClient extends AbstractRemotingClient {
    
    public static final Logger LOG = LoggerFactory.getLogger(NettyRemotingClient.class);
    
    /** 连接的netty server的IP */
    private String ip;
    
    /** 连接的netty server的端口 */
    private int port;
    
    /** IO操作线程数 */
    private int ioThreadCnt;
    
    
    private Bootstrap bootstrap;
    private EventLoopGroup group;
    
    @Setter
    private ChannelInitializer<NioSocketChannel> channelInitializer;
    
    @Getter
    private Channel clientChannel;
    
    public NettyRemotingClient(String ip, int port, int ioThreadCnt) {
        this.ip = ip;
        this.port = port;
        this.ioThreadCnt = ioThreadCnt;
    }
    
    /**
     * 初始化
     */
    public void init() {
        check();
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup(this.ioThreadCnt);
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(channelInitializer);
    }
    
    /**
     * 检查参数
     */
    private void check() {
        if(null == channelInitializer) {
            throw new RpcException("ChannelHandler is null, need to set.");
        }
    }
    
    /**
     * 
     */
    @Override
    public Thread open() {
        try {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).sync();
            clientChannel = channelFuture.channel();
        } catch (InterruptedException e) {
            LOG.error("Connect to server failed.", e);
            System.exit(-1);
        }
        
        //连接已经完成
        setOpenDown();
        return waitForChannelCloseInAnotherThread(clientChannel);
    }

    @Override
    public void close() {
        try{
            group.shutdownGracefully();
            clientChannel.close();
        } catch(Exception e) {
        }
    }
}
