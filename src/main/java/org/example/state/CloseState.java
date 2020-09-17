package org.example.state;


import org.example.cb.AbstractCircuitBreaker;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class CloseState implements State {

    /**
     * 进入当前状态的初始化时间
     */
    private long stateTime = System.currentTimeMillis();

    /**
     * 关闭状态，失败计数器，以及失败计数器初始化时间
     */
    private AtomicInteger failNum = new AtomicInteger(0);
    private AtomicInteger totalNum = new AtomicInteger(0);

    public String getStateName() {
        // 获取当前状态名称
        return this.getClass().getSimpleName();
    }

    public void checkAndSwitchState(AbstractCircuitBreaker cb) {

        // 阀值判断，如果失败到达阀值，切换状态到打开状态
        long maxFailNum = Long.valueOf(cb.getFailRateForClose().split("/")[0]);
        if (failNum.get() == maxFailNum) {

            cb.setState(new OpenState());
            System.out.println(cb);
            System.out.println(new Date(System.currentTimeMillis()) + " >>>>>>> 熔断器已开启");
            //延时关闭
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    cb.setState(new CloseState());
                    System.out.println(new Date(System.currentTimeMillis()) + " >>>>>>> 熔断器已关闭");
                }
            }, cb.getTimeForOpen() * 1000);
        }
    }

    public boolean checkBeforeService(AbstractCircuitBreaker cb) {
        //超过统计区间重新统计
        long totalNumC = Long.valueOf(cb.getFailRateForClose().split("/")[1]);
        if (totalNum.incrementAndGet() >= totalNumC) {
            totalNum.set(0);
            failNum.set(0);
        }
        return true;
    }

    public void countFailNum(AbstractCircuitBreaker cb) {
        //检查统计量是否超出区间
        long totalNumC = Long.valueOf(cb.getFailRateForClose().split("/")[1]);

        if (totalNum.get() >= totalNumC) {
            totalNum.set(0);
            failNum.set(0);
        }
        // 失败计数
        int failCount = failNum.incrementAndGet();
        System.out.println("请求失败-- count " + failCount);

        // 检查是否切换状态
        checkAndSwitchState(cb);
    }
}
