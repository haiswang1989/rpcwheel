package com.wheel.rpc.container.handler;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.wheel.rpc.cache.RegistryCache;
import com.wheel.rpc.container.limiter.Limiter;
import com.wheel.rpc.core.exception.LimiterException;
import com.wheel.rpc.core.model.RpcRequest;
import com.wheel.rpc.core.model.RpcResponse;
import com.wheel.rpc.core.model.RpcStatus;
import com.wheel.rpc.core.model.ServiceGovernanceModel;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


/**
 * 方法的限流
 * 
 * 限流也在服务端进行控制
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月7日 下午5:58:45
 */
public class LimiterHandler extends ChannelInboundHandlerAdapter {
    
    public static final Logger LOG = LoggerFactory.getLogger(LimiterHandler.class);
    
    private LoadingCache<String, Limiter> rateLimiters = CacheBuilder.newBuilder().concurrencyLevel(16)
            .expireAfterAccess(7, TimeUnit.DAYS).build(new CacheLoader<String, Limiter>() {
                @Override
                public Limiter load(String cacheKey) throws Exception {
                    String[] keys = StringUtils.split(cacheKey, '_');
                    int limiter = Integer.parseInt(keys[2]);
                    Limiter rateLimiter = new Limiter(limiter);
                    return rateLimiter;
                }
            });
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcRequest rpcRequest = (RpcRequest)msg;
        String serviceName = rpcRequest.getServiceName();
        String methodName = rpcRequest.getMethodName();
        ServiceGovernanceModel serviceGovernanceModel = RegistryCache.getServiceGovernance(serviceName);
        int limiter = serviceGovernanceModel.getRateLimiter(methodName);
        if(0 != limiter) { //0表示不限流直接过
            String cacheKey = buildKey(serviceName, methodName, limiter);
            Limiter rateLimiter = rateLimiters.get(cacheKey);
            rateLimiters.put(cacheKey, rateLimiter);
            boolean allow = rateLimiter.tryAcquire();
            if(!allow) {
                //被限流挡住了
                String msgInfo = String.format("Request rate is too fast, trigger limiter , serviceName : %s ,methodName : %s", serviceName, methodName);
                LOG.info(msgInfo);
                LimiterException ex = new LimiterException(msgInfo);
                RpcResponse rpcResponse = new RpcResponse();
                rpcResponse.setE(ex);
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                rpcResponse.setStatus(RpcStatus.ERROR);
                ctx.writeAndFlush(rpcResponse);
                return;
            }
        }
        
        ctx.fireChannelRead(msg);
    }
    
    /**
     * 
     * @param serviceName
     * @param methodName
     * @param limiter
     * @return
     */
    private String buildKey(String serviceName, String methodName, int limiter) {
        return new StringBuilder(30)
                .append(serviceName)
                .append("_")
                .append(methodName)
                .append("_")
                .append(limiter)
                .toString();
    }
}
