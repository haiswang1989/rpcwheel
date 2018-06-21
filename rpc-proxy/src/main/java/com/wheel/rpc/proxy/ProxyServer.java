package com.wheel.rpc.proxy;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;

import com.wheel.rpc.communication.client.impl.netty.NettyRemotingClient;
import com.wheel.rpc.communication.server.impl.netty.NettyRemotingServer;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.proxy.handler.client.ProxyAsClientHandler;
import com.wheel.rpc.proxy.handler.server.ProxyAsServerChildHandler;
import com.wheel.rpc.registry.IRegistry;
import com.wheel.rpc.registry.RegistryFactory;

import lombok.Setter;

/**
 * 代理服务
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 上午9:52:58
 */
public class ProxyServer {
    
    /** proxy server端的worker线程数 */
    private int serverWorkerThreadCount;
    
    /** proxy server端的boss线程数 */
    private int serverBossThreadCount;
    
    /** proxy server端监听的端口 */
    private int serverPort;
    
    /** 当前proxy代理的服务列表 */
    private List<Class<?>> proxyServices;
    
    /** 注册中心 */
    private IRegistry registry;
    
    /** 注册中心的config信息 */
    @Setter
    private RegistryConfigBean registryConfigBean;
    
    public ProxyServer(int serverWorkerThreadCountArgs, int serverBossThreadCountArgs, int serverPortArgs, List<Class<?>> proxyServicesArgs) {
        this.serverWorkerThreadCount = serverWorkerThreadCountArgs;
        this.serverBossThreadCount = serverBossThreadCountArgs;
        this.serverPort = serverPortArgs;
        this.proxyServices = proxyServicesArgs;
    }
    
    /**
     * 启动前的初始化
     */
    public void init(int proxy2ServerWorkerCnt) {
        check();
        registry = RegistryFactory.createRegistry(registryConfigBean);
        ProxyServiceCache.init(proxyServices, registry);
        ConcurrentHashMap<String, List<ServiceProviderNode>> servicesProviders = ProxyServiceCache.allServicesProviders();
        //初始化与各个服务的提供者的连接
        for (Map.Entry<String, List<ServiceProviderNode>> entry : servicesProviders.entrySet()) {
            String serviceName = entry.getKey();
            List<ServiceProviderNode> providerNodes = entry.getValue();
            ConcurrentHashMap<ServiceProviderNode, NettyRemotingClient> proxyClients = new ConcurrentHashMap<>();
            for (ServiceProviderNode serviceProviderNode : providerNodes) {
                //创建Proxy与服务提供者的连接
                NettyRemotingClient nettyRemotingClient = createRemotingClient(serviceProviderNode, proxy2ServerWorkerCnt);
                proxyClients.put(serviceProviderNode, nettyRemotingClient);
            }
            
            //设置到缓存中
            ProxyServiceCache.setProxyServicesRemotingClients(serviceName, proxyClients);
        }
    }
    
    /**
     * 创建与provider的连接
     * @param serviceProviderNode
     * @param ioThreadCnt
     * @return
     */
    private NettyRemotingClient createRemotingClient(ServiceProviderNode serviceProviderNode, int ioThreadCnt) {
        NettyRemotingClient proxyClient = new NettyRemotingClient(serviceProviderNode.getHostname(), serviceProviderNode.getPort(), ioThreadCnt);
        proxyClient.setChannelInitializer(new ProxyAsClientHandler());
        proxyClient.init();
        proxyClient.open();
        proxyClient.waitForDown();
        return proxyClient;
    }
    
    
    /**
     * check
     */
    private void check() {
        if(null == registryConfigBean) {
            throw new RpcException("Registry config can not be null.");
        }
        
        if(CollectionUtils.isEmpty(proxyServices)) {
            throw new RpcException("Proxy services list can not be emptry.");
        }
    }
    
    /**
     * 初始化代理的服务
     */
    public Thread startProxyServer() {
        NettyRemotingServer proxyServer = new NettyRemotingServer(serverWorkerThreadCount, serverBossThreadCount, serverPort);
        proxyServer.setChildChannelInitializer(new ProxyAsServerChildHandler());
        proxyServer.init();
        Thread waitCloseThread = proxyServer.open();
        proxyServer.waitForDown();
        return waitCloseThread;
    }
}
