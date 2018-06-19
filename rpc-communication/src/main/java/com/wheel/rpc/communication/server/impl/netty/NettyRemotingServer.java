package com.wheel.rpc.communication.server.impl.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.server.impl.AbstractRemotingServer;
import com.wheel.rpc.core.common.CommonUtils;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 基于Netty的服务端
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月6日 下午4:31:58
 */
public class NettyRemotingServer extends AbstractRemotingServer {
    
    public static final Logger LOG = LoggerFactory.getLogger(NettyRemotingServer.class);
    
    private static final int THREAD_COUNT_WORKER = 10;
    private static final int THREAD_COUNT_BOSS = 1;
    
    private ServerBootstrap server; 
    
    /** 负责处理Client的请求 */ 
    private EventLoopGroup bossGroup;
    
    /** 负责IO的操作请求 */
    private EventLoopGroup workerGroup;
    
    private int port;
    
    private ChannelFuture channelFuture;
    
    public NettyRemotingServer(int port) {
        this(THREAD_COUNT_WORKER, THREAD_COUNT_BOSS, port);
    }
    
    public NettyRemotingServer(int workerThreadCount, int bossThreadCount, int port) {
        this.bossGroup = new NioEventLoopGroup(bossThreadCount);
        this.workerGroup = new NioEventLoopGroup(workerThreadCount);
        this.port = port;
        this.server = new ServerBootstrap();
    }
    
    public NettyRemotingServer init() {
        server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
        return this;
    }
    
    /**
     * handler是作用于client端连接server端时
     * @param handler
     * @return
     */
    public NettyRemotingServer handler(ChannelHandler handler) {
        server.handler(handler);
        return this;
    }
    
    /**
     * childHandler是作用于 client端与server端的IO请求
     * @param handler
     * @return
     */
    public NettyRemotingServer childHandler(ChannelHandler handler) {
        server.childHandler(handler);
        return this;
    }
    
    public <T> NettyRemotingServer childOption(ChannelOption<T> childOption, T value) {
        server.childOption(childOption, value);
        return this;
    }
    
    public <T> NettyRemotingServer option(ChannelOption<T> option, T value) {
        server.option(option, value);
        return this;
    }
    
    @Override
    public void open() {
        try {
            channelFuture = server.bind(CommonUtils.getLocalAddressIp(), port).sync();
        } catch (InterruptedException e) {
            LOG.error("Netty server start failed.", e);
            System.exit(-1);
        }
        
        System.out.println("Container start success.");
        
        //等待关闭
        try {
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOG.error("Server wait for close interrupted.", e);
        }
        
        System.out.println("Container closed.");
    }

    @Override
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        //关闭通道
        channelFuture.channel().close();
    }
}
