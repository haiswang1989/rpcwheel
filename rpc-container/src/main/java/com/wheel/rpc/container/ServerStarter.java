package com.wheel.rpc.container;

import java.util.ArrayList;
import java.util.List;

import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.config.bean.ServiceConfigBean;
import com.wheel.rpc.core.test.IHello;
import com.wheel.rpc.core.test.impl.HelloImpl;

/**
 * 启动RPC的服务
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月8日 下午2:11:24
 */
public class ServerStarter {
    
    
    public static void main(String[] args) {
        
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        registryConfigBean.setProtocol("zookeeper");
        registryConfigBean.setConnection("192.168.56.101:2181");
        
        IHello ref = new HelloImpl();
        ServiceConfigBean<IHello> serviceConfigBean = new ServiceConfigBean<IHello>(IHello.class, ref);
        
        List<ServiceConfigBean<?>> servicesArgs = new ArrayList<>();
        servicesArgs.add(serviceConfigBean);
        
        RpcServer rpcServer = new RpcServer(8888, 20, 1, servicesArgs);
        rpcServer.setRegistryConfigBean(registryConfigBean);
        rpcServer.init();
        rpcServer.open();
        
        rpcServer.register();
    }
}
