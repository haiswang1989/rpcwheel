package com.wheel.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.client.common.ClientConstants;
import com.wheel.rpc.client.proxy.ProxyFactory;
import com.wheel.rpc.communication.channel.IRpcWriteChannel;
import com.wheel.rpc.communication.channel.impl.NettyRpcWriteChannel;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.test.IHello;

import io.netty.channel.Channel;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午11:02:57
 */
public class RpcClientStarter {
    
    public static final Logger LOG = LoggerFactory.getLogger(RpcClientStarter.class);
    
    public static void main(String[] args) {
        
        String ip = CommonUtils.getLocalAddressIp();
        int port = 0;
        int ioThreadCnt = 0;
        
        Properties prop = new Properties();
        InputStream is = null;
        try {
            is = RpcClientStarter.class.getClassLoader().getResourceAsStream("client.properties");
            prop.load(is);
            port = Integer.parseInt(prop.getProperty(ClientConstants.PROXY_RPC_PORT));
            ioThreadCnt = Integer.parseInt(prop.getProperty(ClientConstants.CLIENT_RPC_NETTY_THREAD_WORKER));
        } catch (IOException e) {
            LOG.error("", e);
            System.exit(-1);
        } finally {
            if(null!=is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        
        RpcClient rpcClient = new RpcClient(ip, port, ioThreadCnt);
        rpcClient.open();
        
        Channel channel = rpcClient.getChannel();
        IRpcWriteChannel rpcWriteChannel = new NettyRpcWriteChannel(channel);
        IHello proxyRef = ProxyFactory.createProxy(IHello.class, rpcWriteChannel);
        
        int callCnt = 0;
        while(true) {
            int count = 0;
            while(count++ < 305) {
                try {
                    proxyRef.sayHello("wanghaisheng");
                    System.out.println(callCnt++);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            
            try {
                Thread.sleep(1000l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
