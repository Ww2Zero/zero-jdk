package com.zero.test.producercustomer.lock;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Customer implements Runnable {
    private Lock lock;
    private Condition condition;
    private Lock fullLock;
    private Condition fullCon;

    private AtomicInteger inventory;
    private int maxInventory;


    public Customer(AtomicInteger inventory, int max, Lock lock, Condition condition, ReentrantLock fullLock, Condition fullCon) {
        this.inventory = inventory;
        this.lock = lock;
        this.condition = condition;
        this.fullCon = fullCon;
        this.fullLock = fullLock;
        this.maxInventory = max;
    }

    @Override
    public void run() {
        while (true) {
            ThreadUtil.sleep(MathUtil.random(1000));
            consume();
        }

    }

    public void consume() {
        lock.lock();
        try {
            while (inventory.get() == 0) {
                condition.await();
            }
            System.out.println("去除⼀个商品库存，总库存为：" + inventory.decrementAndGet());
            condition.signalAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            lock.unlock();
        }

        if (inventory.get() == 0) {
            try {
                fullLock.lockInterruptibly();
                fullCon.signalAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                fullLock.unlock();
            }
        }
    }
}
