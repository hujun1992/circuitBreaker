package org.example.cb;


/**
 * 基于区间统计，每50次请求存在10次失败，有临界点问题
 */
public class FailRateCircuitBreakerV2 extends AbstractCircuitBreaker {


    public FailRateCircuitBreakerV2(String failRateForClose, int timeForOpen) {
        this.failRateForClose = failRateForClose;
        this.timeForOpen = timeForOpen;
    }


    @Override
    public boolean protectedCodeBefore() {

        return getState().checkBeforeService(this);

    }


    @Override
    public void protectedCodeSuccess() {

    }

    @Override
    public void protectedCodeFail() {
        getState().countFailNum(this);


    }

}
