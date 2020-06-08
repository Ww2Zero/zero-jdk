package com.zero.test.producercustomer.object;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

    private final Object lock;

    private AtomicInteger inventory;

    private int maxInventory;

    public Producer(Object lock, AtomicInteger inventory, int maxInventory) {
        this.lock = lock;
        this.inventory = inventory;
        this.maxInventory = maxInventory;
    }

    @Override
    public void run() {
        while (true) {
            ThreadUtil.sleep(MathUtil.random(1000));
            produce();
        }
    }

    private void produce() {
        synchronized (lock) {
            try {
                while (inventory.get() == maxInventory) {
                    lock.wait();
                }
                System.out.println("放⼊⼀个商品库存，总库存为：" + inventory.incrementAndGet());
                lock.notify();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
