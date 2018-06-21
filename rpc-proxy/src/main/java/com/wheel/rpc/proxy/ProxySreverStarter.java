package com.wheel.rpc.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.test.IHello;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月21日 上午11:36:25
 */
public class ProxySreverStarter {
    
    public static final Logger LOG = LoggerFactory.getLogger(ProxySreverStarter.class);
    
    public static void main(String[] args) {
        
        InputStream is = ProxyServer.class.getClassLoader().getResourceAsStream("proxy.properties");
        Properties prop = new Properties();
        try {
            prop.load(is);
        } catch (IOException e1) {
            LOG.error("", e1);
            System.exit(-1);
        }
        
        int port = Integer.parseInt(prop.getProperty("proxy.rpc.port"));
        int workerCnt = Integer.parseInt(prop.getProperty("proxy.rpc.netty.thread.worker"));
        int bossCnt = Integer.parseInt(prop.getProperty("proxy.rpc.netty.thread.boss"));
        int proxy2ServerWorkerCnt = Integer.parseInt(prop.getProperty("proxy2server.rpc.netty.thread.worker"));
        
        List<Class<?>> proxyServicesArgs = new ArrayList<>();
        proxyServicesArgs.add(IHello.class);
        
        //配置中心信息
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        registryConfigBean.setProtocol("zookeeper");
        registryConfigBean.setConnection("192.168.56.101:2181");
        
        ProxyServer proxyServer = new ProxyServer(workerCnt, bossCnt, port, proxyServicesArgs);
        proxyServer.setRegistryConfigBean(registryConfigBean);
        proxyServer.init(proxy2ServerWorkerCnt);
        proxyServer.startProxyServer();
    }

}
