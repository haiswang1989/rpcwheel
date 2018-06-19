package com.wheel.rpc.proxy.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 路由的handler
 * 
 * 从所有的Provider中选取部分Provider
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午5:20:24
 */
public class RouterHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }
}
