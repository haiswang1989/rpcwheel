package com.wheel.rpc.core.exception;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午3:03:48
 */
public class RpcException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(String message) {
        super(message);
    }

    public RpcException(Throwable cause) {
        super(cause);
    }

    public RpcException() {
        super();
    }
}
