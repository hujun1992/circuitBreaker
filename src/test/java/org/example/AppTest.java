package org.example;

import static org.junit.Assert.assertTrue;

import org.example.cb.CircuitBreaker;
import org.example.cb.CircuitBreakerRunner;
import org.example.cb.FailRateCircuitBreaker;
import org.example.remote.BillService;
import org.example.remote.RemoteService;
import org.example.state.CloseState;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    CountDownLatch countDownLatchStart = new CountDownLatch(1);

    class MytThread implements Runnable{
        private CircuitBreaker circuitBreaker;
        private RemoteService service;
        public MytThread(CircuitBreaker circuitBreaker, RemoteService service) {
            this.circuitBreaker= circuitBreaker;
            this.service= service;
        }

        @Override
        public void run() {
            try {
                //线程等待
                countDownLatchStart.await();
                for(int i=0; i < 1; i++) {

                    String result = CircuitBreakerRunner.run(circuitBreaker, service);
//                    System.out.println(System.currentTimeMillis() + ": "+ result);
                }
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
     * 模拟100次并发请求
     */
    @Test
    public void test() throws TimeoutException {

        RemoteService service = new BillService();
        //定义熔断器
        CircuitBreaker circuitBreaker = new FailRateCircuitBreaker("5/50", 20, service);

        //启动100个线程，每个线程请求10次
        for(int i =0; i< 50; i ++){
            Thread thread = new Thread(new MytThread(circuitBreaker, service));
            thread.start();
        }
        countDownLatchStart.countDown();
        while(true){

        }



    }
    @Test
    public void test2(){

        RemoteService service = new BillService();
        //定义熔断器
        CircuitBreaker circuitBreaker = new FailRateCircuitBreaker("5/10", 20, service);


            for (int i = 0; i < 20; i++) {
                try {
                String result = CircuitBreakerRunner.run(circuitBreaker, service);
                }catch (Exception e){
                     e.printStackTrace();
                }
            }

//        countDownLatchStart.countDown();
        while(true){

        }



    }


}
