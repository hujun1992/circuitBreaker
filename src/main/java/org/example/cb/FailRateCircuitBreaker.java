package org.example.cb;

import org.example.count.SlidingWindowCounter;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于滑动数组统计 每50次请求10次失败
 */
public class FailRateCircuitBreaker extends AbstractCircuitBreaker {
    AtomicInteger num = new AtomicInteger();
    AtomicInteger failNum = new AtomicInteger();
    /**
     * 熔断器默认当前状态
     */
    public volatile String state = "close";
    public String getState() {
        return state;
    }

    public void setState(String state) {
        String currentState = getState();
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

    protected void toClosedState(){
        state = "close";
        String[] rate = failRateForClose.split("/");
        int size = Integer.valueOf(rate[1]);
        slidingWindowCounter.reset(size);
    }
    protected void toOpenState(){
        state = "open";
        System.out.println(new Date(System.currentTimeMillis())+" >>>>>>> 熔断器已开启");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                state = "close";
                System.out.println(new Date(System.currentTimeMillis())+" >>>>>>> 熔断器已关闭");
            }
        }, timeForOpen * 1000);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Exception {
        //让state内存可见

//        protectedCodeBefore();


        Object result = null;
        try {
            if(this.getState().equals("close")) {
                result = method.invoke(target, args);
            }else{
                System.out.println("拦截业务方法");
            }
            //服务调用成功计数
            protectedCodeSuccess();
        } catch (Exception e) {
            //服务调用失败计数
            protectedCodeFail();

            //熔断未开启抛出原运行时异常
            if ("close".equals(state)) {
                throw e;
            }
            //第一次熔断开启
//            if ("open".equals(state)) {
//               protectedCodeBefore();
//            }
        }
        return result;
    }


    @Override
    public void open() {
        state = "open";


    }
    @Override
    public void close() {
        state = "close";
    }

    @Override
    public void protectedCodeBefore() throws Exception{
        synchronized (this) {
            if ("close".equals(state)) {
                System.out.println("熔断器关闭状态->接口调用次数 " + num.incrementAndGet());
            }
            if ("open".equals(state)) {
                throw new TimeoutException("服务接口已熔断，已触发告警，请稍等！");
            }
        }
    }

    @Override
    public void protectedCodeSuccess() {
        if("close".equals(state)) {
            slidingWindowCounter.add(0);
        }
    }

    @Override
    public synchronized void protectedCodeFail() {
//        System.out.println("熔断器关闭状态->接口调用失败次数 " + failNum.incrementAndGet());
        if("close".equals(state)) {
            slidingWindowCounter.add(1);
            String[] rate = getFailRateForClose().split("/");
            int failNum = Integer.valueOf(rate[0]);
            if (slidingWindowCounter.totalCount() == failNum) {
                //熔断器开启
                toOpenState();

            }
        }


    }

    @Override
    public boolean canPassCheck() {
        return getState().equals("close");
    }
}
