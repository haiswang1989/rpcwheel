package com.wheel.rpc.container;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.communication.server.impl.netty.NettyRemotingServer;
import com.wheel.rpc.container.common.ServicesRefCache;
import com.wheel.rpc.container.handler.ServerChildChannelInitializer;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.config.bean.ServiceConfigBean;
import com.wheel.rpc.core.exception.RpcException;
import com.wheel.rpc.core.model.RegistryModel;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.notify.INotify;
import com.wheel.rpc.notify.NotifyFactory;
import com.wheel.rpc.registry.IRegistry;
import com.wheel.rpc.registry.RegistryFactory;

import lombok.Setter;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:22:01
 */
public class RpcServer {
    
    /** 服务的worker线程数 */
    private int workerThreadCnt;
    
    /** 服务的boss线程数 */
    private int bossThreadCnt;
    
    /** 服务监听的端口 */
    private int port;
    
    /** 服务列表 */
    private List<ServiceConfigBean<?>> services;
    
    @Setter
    private RegistryConfigBean registryConfigBean;
    
    /** 注册中心 */
    private IRegistry registry;
    
    /** Netty Server */
    private NettyRemotingServer server;
    
    /** 注册的MODELS */
    private List<RegistryModel> registryModels;
    
    public RpcServer(int port, List<ServiceConfigBean<?>> servicesArgs) {
        this(port, 0, 0, servicesArgs);
    }
    
    public RpcServer(int port, int workerThreadCnt, int bossThreadCnt, List<ServiceConfigBean<?>> servicesArgs) {
        this.port = port;
        this.workerThreadCnt = workerThreadCnt;
        this.bossThreadCnt = bossThreadCnt;
        this.services = servicesArgs;
        registryModels = new ArrayList<>();
        
    }
    
    /**
     * 初始化
     */
    public void init() {
        check();
        registry = RegistryFactory.createRegistry(registryConfigBean);
        RegistryCache.setRegistry(registry);
        RegistryCache.initByServiceConfigBean(services);
        
    }
    
    /**
     * check
     */
    private void check() {
        if(null == registryConfigBean) {
            throw new RpcException("Registry config can not be null.");
        }
        
        if(CollectionUtils.isEmpty(services)) {
            throw new RpcException("Service list can not be null.");
        }
    }
    
    /**
     * 
     */
    public void open() {
        server = new NettyRemotingServer(workerThreadCnt, bossThreadCnt, port);
        server.setChildChannelInitializer(new ServerChildChannelInitializer());
        server.init();
        server.open();
        server.waitForDown();
    }
    
    /**
     * 
     */
    public void close() {
        //先将自己从配置中心下线,Proxy端就不再看到自己
        for (RegistryModel registryModel : registryModels) {
            registry.unregist(registryModel);
        }
        //断开与配置中心的连接
        registry.close();
        //关闭server端
        server.close();
    }
    
    /**
     * 将自己注册到注册中心
     */
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
            
            //缓存起来,用于后面unRegistry
            registryModels.add(registryModel);
        }
    }
    
    /**
     * 监听变化
     */
    public void subscribe() {
        for (ServiceConfigBean<?> serviceConfigBean : services) {
            String serviceName = serviceConfigBean.getInterfaceClazz().getName();
            INotify notify = NotifyFactory.createNotify(serviceName, registryConfigBean);
            registry.subscribe(notify);
        }
    }
}
