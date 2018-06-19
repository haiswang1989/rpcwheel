package com.wheel.rpc.core.handler;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * 处理"黏包解包"问题
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午4:23:56
 */
public class DecodeHandler extends LengthFieldBasedFrameDecoder {
    
    /**
     *   head           body
     * |-------|----------------------|
     *   12         "wanghaisheng"
     * 
     * lengthFieldOffset位0
     * lengthFieldLength是4(一个int型是4字节)
     * 
     * @param maxFrameLength 表示数据包最大的长度(字节数)
     * @param lengthFieldOffset 表示描述数据包长度的"长度域"的偏移量   
     * @param lengthFieldLength 表示"长度域"的长度
     */
    public DecodeHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
    
    /**
     * 
     * @param maxFrameLength 同上
     * @param lengthFieldOffset 同上
     * @param lengthFieldLength 同上
     * @param lengthAdjustment 解码后返回的包需要"忽略"的字节的起始位置
     * @param initialBytesToStrip 解码后返回的包需要"忽略"的字节的长度
     */
    public DecodeHandler(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment,
            int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected void callDecode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        Object obj = null;
        try {
            obj = decode(ctx, in);
            out.add(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void decodeLast(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        super.decodeLast(ctx, in, out);
    }
}
