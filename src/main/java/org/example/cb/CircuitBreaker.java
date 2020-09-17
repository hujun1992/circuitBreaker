package org.example.cb;

import java.lang.reflect.InvocationHandler;

public interface CircuitBreaker extends InvocationHandler {
    //开启熔断器
    public void open();
    //关闭熔断器
    public  void close();
    /**
     * 调用受保护代码块之前执行的逻辑
     */
    public void protectedCodeBefore() throws RuntimeException, Exception;

    /**
     * 受保护代码块调用成功执行的逻辑
     */
    public void protectedCodeSuccess();

    /**
     * 受保护代码块调用失败执行的逻辑
     */
    public void protectedCodeFail();
    public boolean canPassCheck();
}
