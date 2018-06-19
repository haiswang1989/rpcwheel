package com.wheel.rpc.container.handler;

import com.wheel.rpc.core.handler.DecodeHandler;
import com.wheel.rpc.core.handler.EncodeHandler;
import com.wheel.rpc.core.handler.serialize.DeserializeHandler;
import com.wheel.rpc.core.handler.serialize.SerializeHandler;
import com.wheel.rpc.core.model.RpcRequest;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Server端的NioServerSocketChannel的Pipeline的初始化 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:34:01
 */
public class ServerChildChannelInitializer extends ChannelInitializer<NioSocketChannel> {
    
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        //inbound的handler注册顺序和执行顺序一致
        //outbound的handler的注册顺序和执行顺序相反
        ch.pipeline().addLast(new EncodeHandler()) //outbound,对字节数组进行编码,将body的长度写入到字节数组中
            .addLast(new SerializeHandler()) //outbound,序列化response
            .addLast(new DecodeHandler(Integer.MAX_VALUE, 0, 4, 0, 4)) //inbound,解码前四个字节表示的是长度,在解码的时候去除
            .addLast(new DeserializeHandler<>(RpcRequest.class)) //inbound,请求的反序列化
            .addLast(new DegradationHandler()) //inbound,降级
            .addLast(new LimiterHandler()) //inbound,限流
            .addLast(new RequestHandler()); //inbound,方法真实的调用
            
    }
}
