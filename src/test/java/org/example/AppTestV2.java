package org.example;

import org.example.cb.CircuitBreaker;
import org.example.cb.CircuitBreakerRunner;
import org.example.cb.FailRateCircuitBreakerV2;
import org.example.proxy.ServiceProxy;
import org.example.remote.BillService;
import org.example.remote.RemoteService;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTestV2 {
    CountDownLatch countDownLatchStart = new CountDownLatch(1);

    class MytThread implements Runnable {
        private ServiceProxy proxy;
        private RemoteService service;

        public MytThread(ServiceProxy serviceProxy, RemoteService service) {
            this.proxy = serviceProxy;
            this.service = service;
        }

        @Override
        public void run() {

            try {
                Thread.sleep(new Random().nextInt(20) * 1000);

                //线程等待
                countDownLatchStart.await();
//                for(int i=0; i < 1; i++) {

                String result = CircuitBreakerRunner.run(proxy, service);
//                    System.out.println(System.currentTimeMillis() + ": "+ result);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }


    /**
     * 模拟并发请求
     */
    @Test
    public void test() throws TimeoutException {

        final RemoteService service = new BillService();
        //定义熔断器
        final CircuitBreaker circuitBreaker = new FailRateCircuitBreakerV2("5/50", 20);
        //代理对象
        final ServiceProxy proxy = new ServiceProxy(circuitBreaker, service);
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(new MytThread(proxy, service));
            thread.start();
        }
        countDownLatchStart.countDown();
        while (true) {

        }


    }

    @Test
    public void test2() throws InterruptedException {

        RemoteService service = new BillService();
        //定义熔断器
        CircuitBreaker circuitBreaker = new FailRateCircuitBreakerV2("5/10", 20);

        ServiceProxy proxy = new ServiceProxy(circuitBreaker, service);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    try {
                        Thread.sleep(1000);
                        String result = CircuitBreakerRunner.run(proxy, service);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thread.start();
        thread.join();


    }


}
