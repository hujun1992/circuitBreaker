package org.example.cb;

import org.example.remote.RemoteService;

import java.lang.reflect.Proxy;
import java.util.concurrent.TimeoutException;

/**
 * 熔断器的执行器
 */
public class CircuitBreakerRunner {
    /**
     * 通过动态代理方式执行 remoteService.call()
     * @param circuitBreaker
     * @param remoteService
     * @throws TimeoutException
     */
    public static String run(CircuitBreaker circuitBreaker, RemoteService remoteService){
        try {
            RemoteService proxyService = (RemoteService) Proxy.newProxyInstance(remoteService.getClass().getClassLoader(), remoteService.getClass().getInterfaces(), circuitBreaker);
            return proxyService.call();
        }catch (Exception e){
            e.printStackTrace();
        }

       return null;
    }
}
