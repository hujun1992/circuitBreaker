package org.example.cb;

import org.example.state.CloseState;
import org.example.state.OpenState;

import java.lang.reflect.Method;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class FailRateCircuitBreaker extends AbstractCircuitBreaker {
    AtomicInteger num = new AtomicInteger();

    Object target;

    public FailRateCircuitBreaker(String failRateForClose, int timeForOpen, Object object) {
        this.failRateForClose = failRateForClose;
        this.timeForOpen = timeForOpen;
        this.target = object;
        //熔断器默认关闭
        toClosedState();

    }

    protected void toClosedState(){
        state = new CloseState(this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        //让state内存可见
        System.out.println(this);
        System.out.println(state instanceof CloseState);
        state.protectedCodeBefore();


        Object result = null;
        try {
            result = method.invoke(target, args);
            //服务调用成功计数
            state.protectedCodeSuccess();
        } catch (Exception e) {
            //服务调用失败计数
            state.protectedCodeFail();

            //熔断未开启抛出原运行时异常
            if (state instanceof CloseState) {
                throw new RuntimeException(e);
            }
            //第一次熔断开启
            if (state instanceof OpenState) {
                state.protectedCodeBefore();
            }
        }finally {
            System.out.println("接口调用次数 " + num.incrementAndGet());

        }
        return result;
    }


    @Override
    public void open() {
        this.setState(new OpenState(this));


    }
    @Override
    public void close() {
        this.setState(new CloseState(this));
    }
}
