package com.wheel.rpc.container;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.container.common.ServerConstants;
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
public class RpcServerStarter {
    
    public static final Logger LOG = LoggerFactory.getLogger(RpcServerStarter.class);
    
    public static void main(String[] args) {
        
        int port = 0;
        int workerCnt = 0;
        int bossCnt = 0;
        
        InputStream is = null;
        Properties prop = new Properties();
        try {
            is = RpcServerStarter.class.getClassLoader().getResourceAsStream("server.properties");
            prop.load(is);
            port = Integer.parseInt(prop.getProperty(ServerConstants.SERVER_RPC_PORT));
            workerCnt = Integer.parseInt(prop.getProperty(ServerConstants.SERVER_RPC_NETTY_THREAD_WORKER));
            bossCnt = Integer.parseInt(prop.getProperty(ServerConstants.SERVER_RPC_NETTY_THREAD_BOSS));
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
        
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        registryConfigBean.setProtocol("zookeeper");
        registryConfigBean.setConnection("192.168.56.101:2181");
        
        IHello ref = new HelloImpl();
        ServiceConfigBean<IHello> serviceConfigBean = new ServiceConfigBean<IHello>(IHello.class, ref);
        
        List<ServiceConfigBean<?>> servicesArgs = new ArrayList<>();
        servicesArgs.add(serviceConfigBean);
        
        RpcServer rpcServer = new RpcServer(port, workerCnt, bossCnt, servicesArgs);
        rpcServer.setRegistryConfigBean(registryConfigBean);
        rpcServer.init();
        rpcServer.open();
        
        rpcServer.register();
    }
}
