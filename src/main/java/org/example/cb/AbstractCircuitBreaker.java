package org.example.cb;


public abstract class AbstractCircuitBreaker implements CircuitBreaker  {


    /**
     * 关闭状态时，多少次请求中发生多少次失败进入开启状态（默认50次请求里发生10次失败）
     */
    public String failRateForClose = "10/50";

    /**
     * 开启状态持续时间（默认熔断10分钟）单位秒
     */
    public int timeForOpen = 600;



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
