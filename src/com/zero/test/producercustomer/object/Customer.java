package com.zero.test.producercustomer.object;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {


    private final Object lock;

    private AtomicInteger inventory;

    public Customer(Object lock, AtomicInteger inventory) {
        this.lock = lock;
        this.inventory = inventory;
    }

    @Override
    public void run() {
        while (true) {
            ThreadUtil.sleep(MathUtil.random(1000));
            consume();
        }
    }

    private void consume() {
        synchronized (lock) {
            try {
                while (inventory.get() == 0) {
                    lock.wait();
                }
                System.out.println("去除⼀个商品库存，总库存为：" + inventory.decrementAndGet());
                lock.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
