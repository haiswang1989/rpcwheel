package com.wheel.rpc.proxy.handler.client;

import com.wheel.rpc.core.handler.DecodeHandler;
import com.wheel.rpc.core.handler.EncodeHandler;
import com.wheel.rpc.core.handler.serialize.DeserializeHandler;
import com.wheel.rpc.core.handler.serialize.SerializeHandler;
import com.wheel.rpc.core.model.RpcResponse;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 
 * Proxy->server端的handler的初始化
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午9:51:45
 */
public class ProxyAsClientHandler extends ChannelInitializer<NioSocketChannel> {
    
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        
        ch.pipeline().addLast(new EncodeHandler()) //out,编码
                     .addLast(new SerializeHandler()) //out,请求序列化
                     .addLast(new DecodeHandler(Integer.MAX_VALUE, 0, 4, 0, 4)) //inbound,解码
                     .addLast(new DeserializeHandler<>(RpcResponse.class)) //inbound,反序列化
                     .addLast(new ResponseAllotHandler()); //inbound,把server端返回的response进分配
        
    }
}
