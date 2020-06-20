package com.zero.test.java.util.concurrent.reentrantlock;

import com.zero.test.util.ThreadUtil;

import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main(String[] args) {

        ReentrantLock lock = new ReentrantLock();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleep(1000);
                try {
                    lock.lock();
                    ThreadUtil.sleep(1000);
                    try {
                        lock.lock();
                        ThreadUtil.sleep(2000);
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleep(1000);
                try {
                    lock.lock();
                    ThreadUtil.sleep(1000);
                    try {
                        lock.lock();
                        ThreadUtil.sleep(2000);
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
