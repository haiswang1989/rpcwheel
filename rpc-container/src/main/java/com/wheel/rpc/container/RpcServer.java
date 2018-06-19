package com.wheel.rpc.container;

import java.net.InetSocketAddress;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;

import com.wheel.rpc.communication.server.impl.netty.NettyRemotingServer;
import com.wheel.rpc.container.common.ServicesRefCache;
import com.wheel.rpc.container.handler.ServerChildChannelInitializer;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.config.bean.ServiceConfigBean;
import com.wheel.rpc.core.model.RegistryModel;
import com.wheel.rpc.core.model.RegistryProtocal;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.registry.IRegistry;
import com.wheel.rpc.registry.impl.zookeeper.ZookeeperRegistry;

import lombok.Setter;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:22:01
 */
public class RpcServer {
    
    private int port;
    
    private int workerThreadCnt;
    
    private int bossThreadCnt;
    
    private List<ServiceConfigBean<?>> services;
    
    @Setter
    private RegistryConfigBean registryConfigBean;
    
    private ZkClient zkClient;
    
    private IRegistry registry;
    
    public RpcServer(int port, List<ServiceConfigBean<?>> servicesArgs) {
        this(port, 0, 0, servicesArgs);
    }
    
    public RpcServer(int port, int workerThreadCnt, int bossThreadCnt, List<ServiceConfigBean<?>> servicesArgs) {
        this.port = port;
        this.workerThreadCnt = workerThreadCnt;
        this.bossThreadCnt = bossThreadCnt;
        this.services = servicesArgs;
        
    }
    
    public void init() {
        //初始化zkclient
        String protocal = registryConfigBean.getProtocol();
        String connection = registryConfigBean.getConnection();
        if(RegistryProtocal.ZOOKEEPER.is(protocal)) {
            zkClient = new ZkClient(connection);
        }
        
        registry = new ZookeeperRegistry(zkClient);
    }
    
    /**
     * 
     */
    public void open() {
        final NettyRemotingServer server = new NettyRemotingServer(workerThreadCnt, bossThreadCnt, port);
        server.setChildChannelInitializer(new ServerChildChannelInitializer());
        server.init();
        server.open();
    }
    
    public void register() {    
        for (ServiceConfigBean<?> serviceConfigBean : services) {
            String serviceName = serviceConfigBean.getInterfaceClazz().getName();
            Object ref = serviceConfigBean.getRef();
            ServicesRefCache.put(serviceName, ref);
            
            RegistryModel registryModel = new RegistryModel();
            registryModel.setService(serviceConfigBean.getInterfaceClazz());
            registryModel.setAddress(new InetSocketAddress(CommonUtils.getLocalAddressIp(), port));
            registryModel.setServiceGovernanceModel(new ServiceGovernanceModel());
            registry.regist(registryModel);
        }
    }
}
