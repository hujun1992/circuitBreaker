package org.example.count;

import java.util.concurrent.ArrayBlockingQueue;

public class SlidingWindowCounter {
    private ArrayBlockingQueue<Integer> queue;

    public SlidingWindowCounter(int size) {
        queue = new ArrayBlockingQueue<Integer>(size);
    }

    public ArrayBlockingQueue getQueue() {
        return queue;
    }

    public void setQueue(ArrayBlockingQueue queue) {
        this.queue = queue;
    }

    /**
     * 向有界队列添加元素 当元素满后移除头部元素重新添加
     */
    public void add(int i) {
        if (!queue.offer(i)) {
            queue.poll();
            queue.offer(i);
        }


    }
    public int getSize(){
        return queue.size();
    }


    public  int totalCount() {
        return queue.stream().mapToInt(item -> item.intValue()).sum();
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    public static void main(String[] args) {


    }


}
