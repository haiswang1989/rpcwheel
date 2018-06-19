package com.wheel.rpc.container.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 方法的限流
 * 
 * 限流也在服务端进行控制
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午5:58:45
 */
public class LimiterHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }
    
}
