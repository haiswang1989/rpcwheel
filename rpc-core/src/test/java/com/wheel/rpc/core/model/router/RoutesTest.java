package com.wheel.rpc.core.model.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.I0Itec.zkclient.ZkClient;
import org.testng.annotations.Test;

import com.alibaba.fastjson.JSONObject;
import com.wheel.rpc.core.model.ServiceGovernanceModel;
import com.wheel.rpc.core.model.ServiceProviderNode;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月28日 下午4:35:45
 */
public class RoutesTest {
    
    @Test
    public void setRoutes() {
        
        ZkClient zkClient = new ZkClient("192.168.56.101:2181");
        ServiceGovernanceModel serviceGovernanceModel = new ServiceGovernanceModel();
        serviceGovernanceModel.setDegradation(false);
        
        //结点权重
        Map<ServiceProviderNode, Integer> nodesWeight = new HashMap<>();
        ServiceProviderNode providerNode = new ServiceProviderNode("hansen-wang.corp.vipshop.com", 8888);
        nodesWeight.put(providerNode, 10);
        serviceGovernanceModel.setNodesWeight(nodesWeight);
        
        //路由
        Target target = new Target();
        target.setAddressIp("hansen-wang.corp.vipshop.com");
        target.setPort(8888);
        
        String targetGroupName = "targetgroup-1";
        TargetGroup targetGroup = new TargetGroup();
        targetGroup.setTargets(new Target[]{target});
        targetGroup.setName(targetGroupName);
        
        Action action = new Action();
        action.setType("forward");
        action.setTargetGroupNames(new String[]{targetGroupName});
        
        Rule rule = new Rule();
        rule.setNot(false);
        rule.setOtherwise(false);
        rule.setExpression("say.*");
        rule.setExpressionKey("method");
        rule.setExpressionType(ExpressionType.REGEXP.getKey());
        
        Route route = new Route();
        route.setAction(action);
        route.setRule(rule);
        
        
        Map<String, TargetGroup> name2TargetGroup = new HashMap<>();
        name2TargetGroup.put(targetGroupName, targetGroup);
        
        List<Route> routeList = new ArrayList<>();
        routeList.add(route);
        
        Routes routes = new Routes();
        routes.setOpen(true);
        routes.setName2TargetGroup(name2TargetGroup);
        routes.setRoutes(routeList);
        
        serviceGovernanceModel.setRoutes(routes);
        
        String data = JSONObject.toJSONString(serviceGovernanceModel);
        String path = "/rpcwheel/com.wheel.rpc.core.test.IHello";
        zkClient.writeData(path, data);
        
    }
    
    @Test
    public void checkRegex() {
        Pattern pattern = Pattern.compile("say.*");
        System.out.println(pattern.matcher("sayHello").matches());
    }
    
}
