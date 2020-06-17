package com.zero.test.producercustomer.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private BlockingQueue queue;

    public Producer(BlockingQueue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        produce();
    }

    public void produce() {
        while (true) {
            String hello = "Hello";
            try {
                queue.put(hello);
                System.out.println("produce剩余容量 = " + queue.remainingCapacity());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }
}
