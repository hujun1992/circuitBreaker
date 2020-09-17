//package org.example.state;
//
//import org.example.cb.AbstractCircuitBreaker;
//
//import java.util.Date;
//import java.util.Timer;
//import java.util.TimerTask;
//import java.util.concurrent.TimeoutException;
//
//public class OpenState implements State {
//    private AbstractCircuitBreaker abstractCircuitBreaker;
//
//    public OpenState(AbstractCircuitBreaker abstractCircuitBreaker) {
//        System.out.println(new Date(System.currentTimeMillis())+" >>>>>>> 熔断器已开启");
//        this.abstractCircuitBreaker = abstractCircuitBreaker;
//        //定时器 达到一定时间熔断器关闭
//        final Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                abstractCircuitBreaker.setState(new CloseState(abstractCircuitBreaker));
//                System.out.println(new Date(System.currentTimeMillis())+" >>>>>>> 熔断器已关闭");
//            }
//        }, abstractCircuitBreaker.timeForOpen * 1000);
////        timer.cancel();
//
//    }
//
//    @Override
//    public  void protectedCodeBefore() throws Exception {
//
//        throw new TimeoutException("服务接口已熔断，已触发告警，请稍等！");
//
//    }
//
//    @Override
//    public void protectedCodeSuccess() {
//
//    }
//
//    @Override
//    public void protectedCodeFail() {
//
//    }
//}
