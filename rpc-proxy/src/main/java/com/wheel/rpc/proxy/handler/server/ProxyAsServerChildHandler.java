package com.wheel.rpc.proxy.handler.server;

import com.wheel.rpc.core.handler.DecodeHandler;
import com.wheel.rpc.core.handler.EncodeHandler;
import com.wheel.rpc.core.handler.serialize.DeserializeHandler;
import com.wheel.rpc.core.handler.serialize.SerializeHandler;
import com.wheel.rpc.core.model.RpcRequest;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * client->proxy的handler初始化
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月11日 上午9:42:21
 */
public class ProxyAsServerChildHandler extends ChannelInitializer<NioSocketChannel> {
    
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ch.pipeline().addLast(new EncodeHandler()) //outbound,编码
                     .addLast(new SerializeHandler()) //outbound
                     .addLast(new DecodeHandler(Integer.MAX_VALUE, 0, 4, 0, 4)) //inbound,解码
                     .addLast(new DeserializeHandler<>(RpcRequest.class)) //inbound,将request进行反序列化
                     .addLast(new CircuitbreakerHandler()) //inbound,熔断
                     .addLast(new RouterHandler()) //inbound,路由
                     .addLast(new LoadbalanceHandler()) //inbound,负载均衡
                     .addLast(new RequestForwardHandler());  //inbound,将请求进行转发,发送到server端
    }
}
