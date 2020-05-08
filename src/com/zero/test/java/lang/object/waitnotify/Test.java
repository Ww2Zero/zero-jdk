package com.zero.test.java.lang.object.waitnotify;

import com.zero.test.util.ThreadUtil;

public class Test {

    public static void main(String[] args) {

        testWaitNotMonitor();
        testNotifyNotMonitor();
        testWaitWithMonitor();
        testNotifyWithMonitor();
        testWaitNotify();
    }

    /**
     * 执行结果：
     * w1 is waiting for get lock   --> 线程w1尝试获取锁，调用synchronized(lock)方法，等待获取锁
     * w1 get lock                  --> 线程w1先获取到锁，synchronized(lock)方法获取到锁，进入方法内部执行
     * n1 is waiting for get lock   --> 线程n1尝试获取锁，调用synchronized(lock)方法，等待获取锁
     * w1 do wait start             --> 线程w1释放锁，进入对象的wait set中
     * n1 get lock                  --> 线程n1获取到锁，synchronized(lock)方法获取到锁，进入方法内部执行
     * n1 do notify start           --> 线程n1调用notify()方法,随机唤醒对象wait set中的等待的线程w1，使w1线程由TIMED_WAITING状态转换为RUNNABLE状态
     * n1 do notify end             --> 线程n1继续进行后续处理，最后释放锁
     * w1 do wait end               --> 线程w1重新获取锁，继续之前未完成的任务，最后释放锁
     */
    private static void testWaitNotify() {
        Object lock = new Object();
        WaitThread w1 = new WaitThread("w1", lock);
        w1.start();
        ThreadUtil.sleep(2000);
        NotifyThread n1 = new NotifyThread("n1", lock);
        n1.start();
    }


    /**
     * 使用synchronized在子线程中获取控制权
     *
     * @return
     */
    private static void testWaitWithMonitor() {
        Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    /**
     * 直接调用对象的wait方法，会抛错java.lang.IllegalMonitorStateException
     * 线程没有获取该对象的控制权（monitor），控制权还在主线程中
     * 需要使用synchronized在子线程中获取控制权
     *
     * @return
     */
    private static void testWaitNotMonitor() {
        Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private static void testNotifyWithMonitor() {
        Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock) {
                    lock.notify();
                }
            }
        }).start();
    }

    /**
     * 直接调用对象的notify方法，会抛错java.lang.IllegalMonitorStateException
     * 线程没有获取该对象的控制权（monitor），控制权还在主线程中
     * 需要使用synchronized在子线程中获取控制权
     *
     * @return
     */
    private static void testNotifyNotMonitor() {
        Object lock = new Object();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lock.notify();
            }
        }).start();
    }
}
