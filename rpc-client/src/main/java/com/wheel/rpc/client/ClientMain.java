package com.wheel.rpc.client;

import com.wheel.rpc.client.proxy.ProxyFactory;
import com.wheel.rpc.core.common.CommonUtils;
import com.wheel.rpc.core.test.IHello;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月14日 上午11:02:57
 */
public class ClientMain {

    public static void main(String[] args) {
        final RpcClient rpcClient = new RpcClient(CommonUtils.getLocalAddressIp(), 8889, 20);
        //开启连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcClient.open();
            }
        }).start();
        
        try {
            Thread.sleep(5000l);
        } catch (InterruptedException e) {
        }
        
        IHello proxyRef = ProxyFactory.createProxy(IHello.class, rpcClient);
        String resp = proxyRef.sayHello("wanghaisheng");
        System.out.println("..." + resp);
    }
}
