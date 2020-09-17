package org.example.remote;

import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * 模拟订单查询的接口
 */
public class BillService implements RemoteService {


    @Override
    public String call() throws Exception {
//        System.out.println("service接口调用次数  " + num.incrementAndGet());
        Random random = new Random();
        //50%概率会超时
        if (random.nextInt(2) == 1) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new TimeoutException();
        }
        else {
            throw new TimeoutException();
//            return queryOrderSuccess();
        }

    }

    @Override
    public String fallback() {
        return null;
    }

    private String queryOrderSuccess() {
        return "{\"id\":112,\"total_amount\":5000,\"status\":\"paid\",\"paid_time\":\"2020/09/16 17:53:33\"}";
    }
}
