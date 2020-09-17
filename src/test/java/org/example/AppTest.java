package org.example;

import org.example.cb.CircuitBreaker;
import org.example.cb.CircuitBreakerRunner;
import org.example.cb.FailRateCircuitBreaker;
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
public class AppTest {
    CountDownLatch countDownLatchStart = new CountDownLatch(1);

    class MytThread implements Runnable {

        private RemoteService service;
        private ServiceProxy serviceProxy;

        public MytThread(ServiceProxy serviceProxy, RemoteService service) {
            this.serviceProxy = serviceProxy;
            this.service = service;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(20) * 1000);
                //线程等待
                countDownLatchStart.await();
//                for(int i=0; i < 1; i++) {

                String result = CircuitBreakerRunner.run(serviceProxy, service);
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

        RemoteService service = new BillService();
        //定义熔断器
        CircuitBreaker circuitBreaker = new FailRateCircuitBreaker("5/50", 20, service);
        ServiceProxy serviceProxy = new ServiceProxy(circuitBreaker, service);
        for (int i = 0; i < 50; i++) {
            Thread thread = new Thread(new MytThread(serviceProxy, service));
            thread.start();
        }
        countDownLatchStart.countDown();
        while (true) {
            // ...
        }


    }

    @Test
    public void test2() {

        RemoteService service = new BillService();
        //定义熔断器
        CircuitBreaker circuitBreaker = new FailRateCircuitBreaker("5/50", 4, service);

        ServiceProxy serviceProxy = new ServiceProxy(circuitBreaker, service);


        for (int i = 0; i < 1000; i++) {
            try {
                Thread.sleep(1000);
                CircuitBreakerRunner.run(serviceProxy, service);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            // ...
        }


    }


}
