package com.zero.test.producercustomer.lock;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {

    private Lock lock;
    private Condition condition;
    private Lock emptyLock;
    private Condition emptyCon;
    private AtomicInteger inventory;
    private int maxInventory;

    public Producer(AtomicInteger inventory, int max, Lock lock, Condition condition, ReentrantLock emptyLock, Condition emptyCon) {
        this.inventory = inventory;
        this.maxInventory = max;
        this.lock = lock;
        this.condition = condition;
        this.emptyCon = emptyCon;
        this.emptyLock = emptyLock;
    }

    @Override
    public void run() {
        while (true) {
            long random = MathUtil.random(1000);
            ThreadUtil.sleep(random);
            produce(String.valueOf(random));
        }

    }

    public void produce(String e) {
        lock.lock();
        try {
            while (inventory.get() == maxInventory) {
                condition.await();
            }
            System.out.println("放⼊⼀个商品库存，总库存为：" + inventory.incrementAndGet());
            condition.signalAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

        if (inventory.get() > 0) {
            try {
                emptyLock.lockInterruptibly();
                emptyCon.signalAll();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } finally {
                emptyLock.unlock();
            }
        }
    }
}
