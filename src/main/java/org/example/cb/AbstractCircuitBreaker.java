package org.example.cb;

import org.example.state.State;

public abstract class AbstractCircuitBreaker implements CircuitBreaker  {
    /**
     * 熔断器默认当前状态
     */
    public volatile State state = null;

    /**
     * 关闭状态时，多少次请求中发生多少次失败进入开启状态（默认50次请求里发生10次失败）
     */
    public String failRateForClose = "10/50";

    /**
     * 开启状态持续时间（默认熔断10分钟）单位秒
     */
    public int timeForOpen = 600;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        State currentState = getState();
        if (currentState.getClass().getSimpleName().equals(state.getClass().getSimpleName())){
            return;
        }
        synchronized (this){
            // 二次判断
            currentState = getState();
            if (currentState.getClass().getSimpleName().equals(state.getClass().getSimpleName())){
                return;
            }
            this.state = state;
        }
    }

    public String getFailRateForClose() {
        return failRateForClose;
    }

    public void setFailRateForClose(String failRateForClose) {
        this.failRateForClose = failRateForClose;
    }

    public int getTimeForOpen() {
        return timeForOpen;
    }

    public void setTimeForOpen(int timeForOpen) {
        this.timeForOpen = timeForOpen;
    }
}
