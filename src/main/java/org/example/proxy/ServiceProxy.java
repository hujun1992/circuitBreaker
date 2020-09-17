package org.example.proxy;

import org.example.cb.AbstractCircuitBreaker;
import org.example.cb.CircuitBreaker;
import org.example.state.CloseState;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;

public class ServiceProxy implements InvocationHandler {
    private CircuitBreaker circuitBreaker;
    private Object target;

    public ServiceProxy(CircuitBreaker circuitBreaker, Object object) {
        this.circuitBreaker = circuitBreaker;
        this.target = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
//        System.out.println(circuitBreaker);
        if (circuitBreaker.protectedCodeBefore()) {
            try {
                System.out.println("执行到业务方法");
                result = method.invoke(target, args);
                circuitBreaker.protectedCodeSuccess();
            } catch (Exception e) {
                //服务调用失败计数
                System.out.println("业务执行失败了");
                circuitBreaker.protectedCodeFail();

                //熔断未开启抛出原运行时异常
//                if (circuitBreaker.getState() instanceof CloseState) {
//                    throw e;
//                }
            }
        } else {
            System.out.println("拦截到业务方法");
//            throw new TimeoutException("服务接口已熔断，已触发告警，请稍等！");
        }

        return result;
    }
}
