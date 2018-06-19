package com.wheel.rpc.communication.channel.impl;

import io.netty.channel.Channel;

/**
 * Netty的channel
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 上午11:52:49
 */
public class NettyRpcWriteChannel extends AbstractRpcWriteChannel {
    
    private Channel nettyChannel;
    
    public NettyRpcWriteChannel(Channel channelArgs) {
        this.nettyChannel = channelArgs;
    }
    
    @Override
    public void write(Object msg) {
        nettyChannel.write(msg);
    }
    
    @Override
    public void writeAndFlush(Object msg) {
        nettyChannel.writeAndFlush(msg);
    }
}
