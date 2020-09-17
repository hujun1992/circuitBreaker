package org.example.cb;

import org.example.state.State;

import java.util.concurrent.TimeoutException;

public interface CircuitBreaker {

    public State getState();

    /**
     * 调用受保护代码块之前执行的逻辑 对代码块进行拦截
     */
    public boolean protectedCodeBefore() throws TimeoutException;

    /**
     * 受保护代码块调用成功执行的逻辑 成功计数
     */
    public void protectedCodeSuccess();

    /**
     * 受保护代码块调用失败执行的逻辑 失败计数
     */
    public void protectedCodeFail();

}
