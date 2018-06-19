package com.wheel.rpc.communication.server.impl.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.communication.server.impl.AbstractRemotingServer;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.exception.RpcException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.Setter;

/**
 * 基于Netty的服务端
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月6日 下午4:31:58
 */
public class NettyRemotingServer extends AbstractRemotingServer {
    
    public static final Logger LOG = LoggerFactory.getLogger(NettyRemotingServer.class);
    
    private ServerBootstrap server; 
    
    /** 负责处理Client的请求 */ 
    private EventLoopGroup bossGroup;
    
    /** 负责IO的操作请求 */
    private EventLoopGroup workerGroup;
    
    /** worker线程数 */
    private int workerThreadCount;
    
    /** boss线程数 */
    private int bossThreadCount;
    
    /** 服务端绑定端口 */
    private int port;
    
    private ChannelFuture channelFuture;
    
    /** child handler的初始化 ,处理IO请求的*/
    @Setter
    private ChannelInitializer<NioSocketChannel> childChannelInitializer;
    
    /** handler的初始化,处理IO连接的 */
    @Setter
    private ChannelInitializer<NioSocketChannel> channelInitializer;
    
    public NettyRemotingServer(int workerThreadCountArgs, int bossThreadCountArgs, int portArgs) {
        this.workerThreadCount = workerThreadCountArgs;
        this.bossThreadCount = bossThreadCountArgs;
        this.port = portArgs;
    }
    
    /**
     * 初始化
     */
    public void init() {
        check();
        bossGroup = new NioEventLoopGroup(bossThreadCount);
        workerGroup = new NioEventLoopGroup(workerThreadCount);
        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(childChannelInitializer);
    }
    
    /**
     * 检查参数
     */
    private void check() {
        if(null == childChannelInitializer) {
            throw new RpcException("Child channelHandler is null, need to set.");
        }
    }
    
    @Override
    public void open() {
        try {
            channelFuture = server.bind(CommonUtils.getLocalAddressIp(), port).sync();
        } catch (InterruptedException e) {
            LOG.error("Netty server start failed.", e);
            System.exit(-1);
        }
        
        //绑定已经完成
        setOpenDown();
        //等待关闭
        waitForChannelCloseInAnotherThread(channelFuture.channel());
    }
    
    @Override
    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        //关闭通道
        channelFuture.channel().close();
    }
}
