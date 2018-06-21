package com.wheel.rpc.communication;

import io.netty.channel.Channel;

/**
 * 
 * <p>Description:</p>
 * @author hansen.wang
 * @date 2018年6月19日 下午3:59:48
 */
public abstract class AbstractRemoting implements IRemoting {
    
    /** open操作是否完成 */
    protected boolean isOpenDown = false;
    protected Object LOCK = new Object();
    
    @Override
    public void waitForDown() {
        if(isOpenDown == false) {
            synchronized (LOCK) {
                while(true) {
                    if(isOpenDown == false) {
                        try {
                            LOCK.wait();
                        } catch (InterruptedException e) {
                        }
                    } else {
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * 设置isOpenDown标记为true,表示open操作完成
     */
    protected void setOpenDown() {
        synchronized (LOCK) {
            isOpenDown = true;
            LOCK.notifyAll();
        }
    }
    
    /**
     * 异步的等待channel被关闭
     * @param channel
     * @return
     */
    protected Thread waitForChannelCloseInAnotherThread(final Channel channel) {
        Thread waitThread = new Thread(new Runnable() {
            @Override
            public void run() {
                channel.closeFuture().syncUninterruptibly();
            }
        });
        waitThread.start();
        return waitThread;
    }
}
