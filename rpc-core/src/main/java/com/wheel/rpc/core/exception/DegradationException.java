package com.wheel.rpc.core.exception;

/**
 * 服务降级的异常
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月26日 上午11:46:19
 */
public class DegradationException extends RpcException {
    
    private static final long serialVersionUID = 1L;
    
    public DegradationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DegradationException(String message) {
        super(message);
    }

    public DegradationException(Throwable cause) {
        super(cause);
    }

    public DegradationException() {
        super();
    }
}
