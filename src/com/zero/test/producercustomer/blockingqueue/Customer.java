package com.zero.test.producercustomer.blockingqueue;

import java.util.concurrent.BlockingQueue;

public class Customer implements Runnable {

    private BlockingQueue queue;

    public Customer(BlockingQueue queue) {
        this.queue = queue;
    }


    @Override
    public void run() {
        custom();
    }

    private void custom() {
        while (true) {
            try {
                Object take = queue.take();
                System.out.println("custom 剩余容量 = " + queue.remainingCapacity());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
