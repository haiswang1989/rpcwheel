package com.wheel.rpc.core.model.router;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月27日 下午2:34:55
 */
@Data
public class Rule {
   
    //不满足分支,该rule计算后返回false后,进入的分支
    private boolean otherwise;
    
    //规则的嵌套
    private List<Rule> rules = new ArrayList<>();
    
    //规则与规则之间的运算符
    private String operatorType = OperatorType.AND.getOperator();
    
    //表达式
    private String expressionType = ExpressionType.STRING.getKey();
    
    //
    private String expression;
    
    //解析后的表达式,比如regexp解析后就是一个Pattern,避免出现重复创建
    private Object parsedExpression;
    
    //是否是"非运算"
    private boolean isNot;
    
    //表达式计算的KEY
    private String expressionKey;
}
