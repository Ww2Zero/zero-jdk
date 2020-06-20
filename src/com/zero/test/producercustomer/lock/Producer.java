package com.zero.test.producercustomer.lock;

import com.zero.test.util.MathUtil;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Producer implements Runnable {

    private ReentrantLock fullLock;
    private Condition fullCon;
    private ReentrantLock emptyLock;
    private Condition emptyCon;
    private AtomicInteger inventory;
    private int maxInventory;

    public Producer(AtomicInteger inventory, int max, ReentrantLock fullLock, Condition fullCon, ReentrantLock emptyLock, Condition emptyCon) {
        this.inventory = inventory;
        this.maxInventory = max;
        this.fullLock = fullLock;
        this.fullCon = fullCon;
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

        try {
            fullLock.lock();
            while (inventory.get() == maxInventory) {
                System.out.println("fullLock.getHoldCount() = " + fullLock.getHoldCount());
                System.out.println("fullLock.getQueueLength() = " + fullLock.getQueueLength());
                fullCon.await();
            }
            System.out.println("放⼊⼀个商品库存，总库存为：" + inventory.incrementAndGet());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            fullLock.unlock();
        }

        if (inventory.get() == maxInventory) {
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
