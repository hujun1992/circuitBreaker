package org.example.cb;

import java.lang.reflect.InvocationHandler;

public interface CircuitBreaker extends InvocationHandler {
    //开启熔断器
    public void open();
    //关闭熔断器
    public  void close();
}
