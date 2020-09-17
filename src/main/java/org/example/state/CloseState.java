package org.example.state;

import org.example.cb.AbstractCircuitBreaker;
import org.example.count.SlidingWindowCounter;

import java.util.concurrent.atomic.AtomicInteger;

public class CloseState implements State {
    AtomicInteger num = new AtomicInteger();
    AtomicInteger failNum = new AtomicInteger();
    private AbstractCircuitBreaker abstractCircuitBreaker;

    private SlidingWindowCounter slidingWindowCounter;

    public CloseState(AbstractCircuitBreaker abstractCircuitBreaker) {

        this.abstractCircuitBreaker = abstractCircuitBreaker;
        String[] rate = abstractCircuitBreaker.getFailRateForClose().split("/");
        int size = Integer.valueOf(rate[1]);
        slidingWindowCounter = new SlidingWindowCounter(size);

    }

    @Override
    public void protectedCodeBefore() {
        System.out.println("熔断器关闭状态->接口调用次数 " + num.incrementAndGet());
    }

    @Override
    public void protectedCodeSuccess() {
        slidingWindowCounter.add(0);
    }

    @Override
    public synchronized void protectedCodeFail() {
        System.out.println("熔断器关闭状态->接口调用失败次数 " + failNum.incrementAndGet());
        slidingWindowCounter.add(1);
        String[] rate = abstractCircuitBreaker.getFailRateForClose().split("/");
        int failNum = Integer.valueOf(rate[0]);

        if (slidingWindowCounter.totalCount() == failNum) {
            //熔断器开启
            abstractCircuitBreaker.setState(new OpenState(abstractCircuitBreaker));
            System.out.println(abstractCircuitBreaker.getState().getClass().getSimpleName());
            System.out.println(abstractCircuitBreaker);
        }


    }
}
