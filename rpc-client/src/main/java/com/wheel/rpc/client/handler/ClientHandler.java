package com.wheel.rpc.client.handler;


import com.wheel.rpc.core.handler.DecodeHandler;
import com.wheel.rpc.core.handler.EncodeHandler;
import com.wheel.rpc.core.handler.serialize.DeserializeHandler;
import com.wheel.rpc.core.handler.serialize.SerializeHandler;
import com.wheel.rpc.core.model.RpcResponse;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * client的NioSocketChannel的Pipeline的初始化
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午10:27:07
 */
public class ClientHandler extends ChannelInitializer<NioSocketChannel> {
    
    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        //inbound的handler注册顺序和执行顺序一致
        //outbound的handler的注册顺序和执行顺序相反
        ch.pipeline().addLast(new EncodeHandler()) //outbound,对字节数组进行编码,将body的长度写入到字节数组中
                        .addLast(new SerializeHandler()) //outbound,对request进行序列化,将对象转换成字节数组
                        .addLast(new DecodeHandler(Integer.MAX_VALUE, 0, 4, 0, 4)) //inbound,解码
                        .addLast(new DeserializeHandler<>(RpcResponse.class)) //inbound,对请求的返回值进行解码
                        .addLast(new ResponseHandler()); //inbound,分配返回值
    }
}
