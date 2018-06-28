package com.wheel.rpc.core.exception;

public class LimiterException extends RpcException {
    
    private static final long serialVersionUID = 1L;
    
    public LimiterException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimiterException(String message) {
        super(message);
    }

    public LimiterException(Throwable cause) {
        super(cause);
    }

    public LimiterException() {
        super();
    }
}
