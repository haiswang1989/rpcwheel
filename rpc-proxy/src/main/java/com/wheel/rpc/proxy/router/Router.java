package com.wheel.rpc.proxy.router;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.ServiceProviderNode;
import com.wheel.rpc.core.model.router.Action;
import com.wheel.rpc.core.model.router.ExpressionType;
import com.wheel.rpc.core.model.router.OperatorType;
import com.wheel.rpc.core.model.router.Route;
import com.wheel.rpc.core.model.router.Routes;
import com.wheel.rpc.core.model.router.Rule;
import com.wheel.rpc.core.model.router.Target;
import com.wheel.rpc.core.model.router.TargetGroup;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 下午2:50:30
 */
public class Router {
    
    public static final Logger LOG = LoggerFactory.getLogger(Router.class);
    
    private Routes routes;
    private RpcRequest rpcRequest;
    
    public Router(Routes routesArgs, RpcRequest rpcRequestArgs) {
        this.routes = routesArgs;
        this.rpcRequest = rpcRequestArgs;
    }
    
    /**
     * 
     * @return
     */
    public List<ServiceProviderNode> doRouter() {
        //路由列表
        List<Route> routeList = routes.getRoutes();
        Route otherwise = null;
        for (Route route : routeList) {
            if(null == route.getRule()) {
                continue;
            }
            
            if(route.getRule().isOtherwise()) {
                otherwise = route;
                continue;
            }
            
            Rule rule = route.getRule();
            boolean calculateResult = calculate(rule);
            if(calculateResult) {
                return getMatchProviders(route);
            }
        }
        
        if(null!=otherwise) {
            return getMatchProviders(otherwise);
        }
        
        return Collections.emptyList();
    }
    
    /**
     * 
     * @param route
     * @return
     */
    private List<ServiceProviderNode> getMatchProviders(Route route) {
        Action action = route.getAction();
        String[] targetGroupNames = action.getTargetGroupNames();
        
        List<ServiceProviderNode> targetProviders = new ArrayList<>();
        
        for (String targetGroupName : targetGroupNames) {
            TargetGroup targetGroup = routes.getTargetGroup(targetGroupName);
            Target[] targets = targetGroup.getTargets();
            
            for (Target target : targets) {
                targetProviders.add(new ServiceProviderNode(target.getAddressIp(), target.getPort()));
            }
        }
        
        return targetProviders;
    }
    
    /**
     * 
     * 
     * @param rule
     * @return
     */
    private boolean calculate(Rule rule) {
        String opType = rule.getOperatorType();
        List<Rule> rules = rule.getRules();
        
        boolean calculateResult;
        if(CollectionUtils.isNotEmpty(rules)) {
            calculateResult = compoundCalculate(rules, opType);
        } else {
            calculateResult = singleCalculate(rule);
        }
        
        return calculateResult;
    }
    
    /**
     * 递归的对rule进行计算
     * 
     * @param rules
     * @param operatorType
     * @return
     */
    public boolean compoundCalculate(List<Rule> rules, String operatorType) {
        boolean calculateResult = false;
        for (Rule rule : rules) {
            if(CollectionUtils.isNotEmpty(rule.getRules())) {
                calculateResult = compoundCalculate(rule.getRules(), rule.getOperatorType());
            } else {
                calculateResult = singleCalculate(rule);
            }
            
            if(OperatorType.AND.getOperator().equalsIgnoreCase(operatorType) && !calculateResult) {
                //如果是AND关系,当前rule计算返回false,那么最后的结果肯定为false,不需要再继续计算了
                return false;
            } else if(OperatorType.OR.getOperator().equalsIgnoreCase(operatorType) && calculateResult) {
                //如果是OR的关系,当前rule计算返回true,那么最后的结果肯定是true,不需要再计算了
                return true;
            }
        }
        
        //AND关系但是calculateResult一直是true,那么就返回true
        //OR关系但是calculateResult一直是false,那么直接返回false
        return OperatorType.AND.getOperator().equalsIgnoreCase(operatorType);
    }
    
    /**
     * 对单个rule进行计算(没有孩子rule了)
     * 
     * @param rule
     * @return
     */
    private boolean singleCalculate(Rule rule) {
        String expressionValue;
        if(rule.getExpressionKey().equals("method")) {
            expressionValue = rpcRequest.getMethodName();
        } else if(rule.getExpressionKey().equals("service")) {
            expressionValue = rpcRequest.getServiceName();
        } else if(rule.getExpressionKey().equals("callerIp")) {
            expressionValue = rpcRequest.getCallerIp();
        } else if(rule.getExpressionKey().equals("callerId")) {
            expressionValue = rpcRequest.getCallerId();
        } else {
            LOG.error(String.format("Unknow expression key , value : %s", rule.getExpressionKey()));
            return false;
        }
        
        return calculateValue(expressionValue, rule);
    }
    
    /**
     * 
     * @param expressionValue
     * @param rule
     * @return
     */
    private boolean calculateValue(String expressionValue, Rule rule) {
        String expressionType = rule.getExpressionType();
        String expression = rule.getExpression();
        boolean calculateReslult = false;
        if(ExpressionType.STRING.getKey().equals(expressionType) || 
                ExpressionType.NUMBER.getKey().equals(expressionType)) {
            calculateReslult = expression.equals(expressionValue);
        } else if(ExpressionType.IP.getKey().equals(expressionType)) {
            calculateReslult = calculateValueOfIp(expressionValue, rule);
        } else if(ExpressionType.REGEXP.getKey().equals(expressionType)) {
            calculateReslult = calculateValueOfRegexp(expressionValue, rule);
        } else if(ExpressionType.PERCENT.getKey().equals(expressionType)) {
            calculateReslult = calculateValueOfPercent(expressionValue, rule);
        } else if(ExpressionType.RANGE.getKey().equals(expressionType)) {
            calculateReslult = calculateValueOfRange(expressionValue, expression);
        } else {
            LOG.error(String.format("Unknow expression type , value : %s", rule.getExpressionKey()));
            calculateReslult = false;
        }
        
        if(rule.isNot()) {
            return !calculateReslult;
        }
        
        return calculateReslult;
    }
    
    /**
     * 
     * @param value
     * @param rule
     * @return
     */
    private boolean calculateValueOfIp(String value, Rule rule) {
        return true;
    }
    
    /**
     * 正则匹配
     * @param value
     * @param rule
     * @return
     */
    private boolean calculateValueOfRegexp(String value, Rule rule) {
        Pattern cachedPattern = null;
        try {
            cachedPattern = (Pattern)rule.getParsedExpression();
            if(null == cachedPattern) {
                cachedPattern = Pattern.compile(rule.getExpression());
            }
            
            //缓存正则
            rule.setParsedExpression(cachedPattern); 
            return cachedPattern.matcher(value).matches();
        } catch(Throwable e) {
            LOG.error(String.format("Parser regex failed, regex : %s", rule.getExpression()), e);
            return false;
        }
    }
    
    /**
     * 
     * @param value
     * @param rule
     * @return
     */
    private boolean calculateValueOfPercent(String value, Rule rule) {
        return true;
    }
    
    /**
     * xxx IN ('','','','')
     * 
     * @param value
     * @param rule
     * @return
     */
    private boolean calculateValueOfRange(String value, String expression) {
        boolean calculateResult = false;
        String[] values = StringUtils.split(expression, ',');
        for (String val : values) {
            calculateResult = val.equals(value);
            if(calculateResult) {
                break;
            }
        }
        
        return calculateResult;
    }
}
