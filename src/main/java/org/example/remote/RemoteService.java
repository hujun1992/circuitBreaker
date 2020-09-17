package org.example.remote;

import java.util.concurrent.TimeoutException;

/**
 * 远程调用接口
 */
public interface RemoteService {
    /**
     * 远程调用方法
     * @return
     * @throws TimeoutException
     */
    String call() throws TimeoutException;

    /**
     * 熔断开启后调用的本地方法
     * @return
     */
    String fallback();

}
