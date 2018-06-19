package com.wheel.rpc.core.common;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import com.google.common.collect.Lists;

/**
 * 公共的辅助类
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月5日 上午9:23:15
 */
public class CommonUtils {
    
    /**
     * 拼接字符串
     * @param params
     * @return
     */
    public static String appendString(Object ...params) {
        if(null==params || 0==params.length) {
            return "";
        }
        
        StringBuilder builder = new StringBuilder();
        for (Object param : params) {
            builder.append(param);
        }
        
        return builder.toString();
    }
    
    /**
     * 获取list2相对于list1 差集
     * 如:
     * list1 {1,2,3,4,5}
     * list2 {3,4,5,6,7}
     * 结果为:
     * {6,7}
     * @param list1
     * @param list2
     * @return
     */
    public static List<String> getSubtraction(List<String> list1, List<String> list2) {
        List<String> list2Copy = Lists.newArrayList(list2);
        list2Copy.removeAll(list1);
        return list2Copy;
    }
    
    /**
     * 获取当前机器的IP地址
     * @return
     */
    public static String getLocalAddressIp() {
        String ip = "127.0.0.1";
        InetAddress loalAddress = null;
        try {
            loalAddress = InetAddress.getLocalHost();
            ip = loalAddress.getHostAddress();
        } catch (UnknownHostException e) {
        }
        
        return ip;
    }
    
    /**
     * 通过服务的名称,获取对应的ZK的路径
     * @param serviceName
     * @return
     */
    public static String getServicePath(String serviceName) {
        return appendString(Constants.RPC_WHEEL_ROOT_PATH, Constants.PATH_SEPARATOR, serviceName);
    }
}
