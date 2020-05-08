package com.zero.test.java.lang.object.waitnotify;

public class NotifyThread extends Thread {

    private String threadName;
    private Object lock;

    public NotifyThread(String threadName, Object lock) {
        this.threadName = threadName;
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println(threadName+" is waiting for get lock ");
        synchronized (lock) {
            System.out.println(threadName+" get lock ");
            System.out.println(threadName+" do notify start");
            lock.notify();
            System.out.println(threadName+" do notify end");
        }
    }
}
