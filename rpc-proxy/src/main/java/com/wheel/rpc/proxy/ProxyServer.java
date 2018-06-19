package com.wheel.rpc.proxy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.I0Itec.zkclient.ZkClient;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.communication.server.impl.netty.NettyRemotingServer;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.model.RegistryProtocal;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.test.IHello;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.handler.client.ProxyAsClientHandler;
import com.wheel.rpc.proxy.handler.server.ProxyAsServerChildHandler;

import io.netty.channel.ChannelOption;
import lombok.Setter;

/**
 * 代理服务
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午9:52:58
 */
public class ProxyServer {
    
    private int serverWorkerThreadCount;
    private int serverBossThreadCount;
    private int serverPort;
    
    /** 当前proxy代理的服务 */
    private List<Class<?>> proxyServices;
    
    /** 与zookeeper的连接 */
    private ZkClient zkClient;
    
    @Setter
    private RegistryConfigBean registryConfigBean;
    
    public ProxyServer(int serverWorkerThreadCountArgs, int serverBossThreadCountArgs, int serverPortArgs, List<Class<?>> proxyServicesArgs) {
        this.serverWorkerThreadCount = serverWorkerThreadCountArgs;
        this.serverBossThreadCount = serverBossThreadCountArgs;
        this.serverPort = serverPortArgs;
        this.proxyServices = proxyServicesArgs;
    }
    
    private void init() {
        //初始化zkclient
        String protocal = registryConfigBean.getProtocol();
        String connection = registryConfigBean.getConnection();
        if(RegistryProtocal.ZOOKEEPER.is(protocal)) {
            zkClient = new ZkClient(connection);
        }
        
        ProxyServiceCache.init(proxyServices, zkClient);
        ConcurrentHashMap<String, List<ServiceProviderNode>> servicesProviders = ProxyServiceCache.getServicesProviders();
        //初始化与各个服务的提供者的连接
        for (Map.Entry<String, List<ServiceProviderNode>> entry : servicesProviders.entrySet()) {
            String serviceName = entry.getKey();
            List<ServiceProviderNode> providerNodes = entry.getValue();
            ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> proxyClients = new ConcurrentHashMap<>();
            for (ServiceProviderNode serviceProviderNode : providerNodes) {
                final NettyRemotingClient proxyClient = new NettyRemotingClient(serviceProviderNode.getHostname(), serviceProviderNode.getPort()); 
                proxyClient.handler(new ProxyAsClientHandler()).option(ChannelOption.TCP_NODELAY, true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        proxyClient.open();
                    }
                }).start();
                
                proxyClients.put(serviceProviderNode, proxyClient);
            }
            
            ProxyServiceCache.putServicesProxyClients(serviceName, proxyClients);
        }
    }
    
    /**
     * 初始化代理的服务
     */
    private Thread startProxyServer() {
        final NettyRemotingServer proxyServer = new NettyRemotingServer(serverWorkerThreadCount, serverBossThreadCount, serverPort);
        proxyServer.init().childHandler(new ProxyAsServerChildHandler()).childOption(ChannelOption.TCP_NODELAY, true);
        //单独开启一个线程启动server
        Thread startProxyServerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                proxyServer.open();
            }
        });
        
        startProxyServerThread.start();
        return startProxyServerThread;
    }
    
    public static void main(String[] args) {
        List<Class<?>> proxyServicesArgs = new ArrayList<>();
        proxyServicesArgs.add(IHello.class);
        
        //配置中心信息
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        registryConfigBean.setProtocol("zookeeper");
        registryConfigBean.setConnection("192.168.56.101:2181");
        
        ProxyServer proxyServer = new ProxyServer(10, 1, 8889, proxyServicesArgs);
        proxyServer.setRegistryConfigBean(registryConfigBean);
        proxyServer.init();
        
        Thread startProxyServerThread = proxyServer.startProxyServer();
        try {
            startProxyServerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
