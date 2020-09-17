package org.example.state;

import org.example.cb.AbstractCircuitBreaker;
import org.example.count.SlidingWindowCounter;

public class CloseState implements State {

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

    }

    @Override
    public void protectedCodeSuccess() {
        slidingWindowCounter.add(0);
    }

    @Override
    public synchronized void protectedCodeFail() {
        slidingWindowCounter.add(1);
        String[] rate = abstractCircuitBreaker.getFailRateForClose().split("/");
        int failNum = Integer.valueOf(rate[0]);

        if (slidingWindowCounter.totalCount() == failNum) {
            //熔断器开启
            abstractCircuitBreaker.setState(new OpenState(abstractCircuitBreaker));
        }


    }
}
