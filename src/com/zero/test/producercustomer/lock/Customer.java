package com.zero.test.producercustomer.lock;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Customer implements Runnable {
    private ReentrantLock emptyLock;
    private Condition emptyCon;
    private ReentrantLock fullLock;
    private Condition fullCon;

    private AtomicInteger inventory;
    private int maxInventory;


    public Customer(AtomicInteger inventory, int max, ReentrantLock emptyLock, Condition emptyCon, ReentrantLock fullLock, Condition fullCon) {
        this.inventory = inventory;
        this.emptyLock = emptyLock;
        this.emptyCon = emptyCon;
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

        try {
            emptyLock.lock();
            while (inventory.get() == 0) {
                System.out.println("emptyLock.getHoldCount() = " + emptyLock.getHoldCount());
                System.out.println("emptyLock.getQueueLength() = " + emptyLock.getQueueLength());
                emptyCon.await();
            }
            System.out.println("去除⼀个商品库存，总库存为：" + inventory.decrementAndGet());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            emptyLock.unlock();
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
