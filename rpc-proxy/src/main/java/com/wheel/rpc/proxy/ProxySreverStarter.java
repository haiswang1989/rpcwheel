package com.wheel.rpc.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.core.config.bean.RegistryConfigBean;
import com.wheel.rpc.core.test.IHello;
import com.wheel.rpc.proxy.common.ProxyServiceCache;
import com.wheel.rpc.registry.IRegistry;
import com.wheel.rpc.registry.RegistryFactory;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月21日 上午11:36:25
 */
public class ProxySreverStarter {
    
    public static final Logger LOG = LoggerFactory.getLogger(ProxySreverStarter.class);
    
    public static void main(String[] args) {
        
        int port = 0;
        int workerCnt = 0;
        int bossCnt = 0;
        int proxy2ServerWorkerCnt = 0;
        
        InputStream is = null;
        Properties prop = new Properties();
        try {
            is = ProxyServer.class.getClassLoader().getResourceAsStream("proxy.properties");
            prop.load(is);
            port = Integer.parseInt(prop.getProperty("proxy.rpc.port"));
            workerCnt = Integer.parseInt(prop.getProperty("proxy.rpc.netty.thread.worker"));
            bossCnt = Integer.parseInt(prop.getProperty("proxy.rpc.netty.thread.boss"));
            proxy2ServerWorkerCnt = Integer.parseInt(prop.getProperty("proxy2server.rpc.netty.thread.worker"));
        } catch (IOException e1) {
            LOG.error("", e1);
            System.exit(-1);
        } finally {
            if(null!=is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        
        
        List<Class<?>> proxyServices = new ArrayList<>();
        proxyServices.add(IHello.class);
        
        //配置中心信息
        RegistryConfigBean registryConfigBean = new RegistryConfigBean();
        registryConfigBean.setProtocol("zookeeper");
        registryConfigBean.setConnection("192.168.56.101:2181");
        
        //构造registry
        IRegistry registry = RegistryFactory.createRegistry(registryConfigBean);
        //配置中心的缓存的初始化
        RegistryCache.setRegistry(registry);
        RegistryCache.init(proxyServices);
        
        //proxy端 service相关的初始化
        ProxyServiceCache.init(proxyServices);
        
        ProxyServer proxyServer = new ProxyServer(workerCnt, bossCnt, port, proxyServices);
        proxyServer.setRegistryConfigBean(registryConfigBean);
        proxyServer.init(proxy2ServerWorkerCnt);
        proxyServer.startProxyServer();
    }

}
