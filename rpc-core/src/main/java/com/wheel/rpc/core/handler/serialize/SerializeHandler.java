package com.wheel.rpc.core.handler.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.serialization.ISerializer;
import com.wheel.rpc.serialization.impl.json.FastjsonSerializer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 
 * 序列化handler
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午3:09:01
 */
public class SerializeHandler extends ChannelOutboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(SerializeHandler.class);
    
    private ISerializer serializer;
    
    public SerializeHandler() {
        serializer = new FastjsonSerializer();
    }
    
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        byte[] bytes = serializer.serialize(msg);
        ctx.writeAndFlush(bytes, promise);
    }
}
