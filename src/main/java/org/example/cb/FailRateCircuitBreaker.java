package org.example.cb;

import org.example.count.SlidingWindowCounter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于滑动数组统计 每50次请求10次失败
 */
public class FailRateCircuitBreaker extends AbstractCircuitBreaker {
    /**
     * 熔断器默认当前状态
     */
    public volatile String status = "close";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        String currentStatus = getStatus();
        if (currentStatus.equals(status)) {
            return;
        }
        synchronized (this) {
            // 二次判断
            currentStatus = getStatus();
            if (currentStatus.equals(status)) {
                return;
            }
            this.status = status;
        }
    }

    private SlidingWindowCounter slidingWindowCounter;

    Object target;

    public FailRateCircuitBreaker(String failRateForClose, int timeForOpen, Object object) {
        this.failRateForClose = failRateForClose;
        this.timeForOpen = timeForOpen;
        this.target = object;
        String[] rate = failRateForClose.split("/");
        int size = Integer.valueOf(rate[1]);
        slidingWindowCounter = new SlidingWindowCounter(size);
        //熔断器默认关闭
        toClosedState();


    }

    protected void toClosedState() {
        status = "close";
        String[] rate = failRateForClose.split("/");
        int size = Integer.valueOf(rate[1]);
        slidingWindowCounter.reset(size);
    }

    protected void toOpenState() {
        status = "open";
        System.out.println(new Date(System.currentTimeMillis()) + " >>>>>>> 熔断器已开启");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                toClosedState();
                System.out.println(new Date(System.currentTimeMillis()) + " >>>>>>> 熔断器已关闭");
            }
        }, timeForOpen * 1000);
    }


    @Override
    public boolean protectedCodeBefore() throws TimeoutException {
        if (getStatus().equals("close")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void protectedCodeSuccess() {
        if ("close".equals(status)) {
            slidingWindowCounter.add(0);
        }
    }

    @Override
    public synchronized void protectedCodeFail() {
//        System.out.println("熔断器关闭状态->接口调用失败次数 " + failNum.incrementAndGet());
        if ("close".equals(status)) {
            slidingWindowCounter.add(1);
            String[] rate = getFailRateForClose().split("/");
            int failNum = Integer.valueOf(rate[0]);
            if (slidingWindowCounter.totalCount() == failNum) {
                //熔断器开启
                toOpenState();

            }
        }


    }

}
