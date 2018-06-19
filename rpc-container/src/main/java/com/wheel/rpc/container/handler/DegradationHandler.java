package com.wheel.rpc.container.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 服务端的"降级"处理
 * 
 * 人工降级,由服务方统一降级,至于客户端的降级处理,需要客户端自己捕获异常
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:18:17
 */
public class DegradationHandler extends ChannelInboundHandlerAdapter {
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ctx.fireChannelRead(msg);
    }
}
