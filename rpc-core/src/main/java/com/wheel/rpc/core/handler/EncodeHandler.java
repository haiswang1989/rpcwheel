package com.wheel.rpc.core.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 序列化完成以后,这边进行编码
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午3:22:51
 */
public class EncodeHandler extends ChannelOutboundHandlerAdapter {
    
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] bytes = (byte[])msg;
        int length = bytes.length;
        //留四个字节存储长度
        ByteBuf byteBuf = Unpooled.buffer(4 + length);
        //把body的长度写入到数组中
        byteBuf.writeInt(length);
        //写入消息体
        byteBuf.writeBytes(bytes);
        ctx.writeAndFlush(byteBuf, promise);
    }
}
