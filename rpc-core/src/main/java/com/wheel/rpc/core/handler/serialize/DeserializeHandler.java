package com.wheel.rpc.core.handler.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.wheel.rpc.serialization.ISerializer;
import com.wheel.rpc.serialization.impl.json.FastjsonSerializer;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 反序列化handler
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午10:34:56
 */
public class DeserializeHandler<T> extends ChannelInboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(DeserializeHandler.class);
    
    private ISerializer serializer;
    
    private Class<T> clazz;
    
    public DeserializeHandler(Class<T> clazz) {
        serializer = new FastjsonSerializer();
        this.clazz = clazz;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf)msg;
        int length = byteBuf.readableBytes();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        T obj = serializer.deSerialize(bytes, clazz);
        ctx.fireChannelRead(obj);
    }
}
