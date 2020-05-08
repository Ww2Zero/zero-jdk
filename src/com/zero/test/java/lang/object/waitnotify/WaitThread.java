package com.zero.test.java.lang.object.waitnotify;

import com.zero.test.util.ThreadUtil;

public class WaitThread extends Thread {

    private String threadName;

    private Object lock;

    public WaitThread(String threadName, Object lock) {
        this.threadName = threadName;
        this.lock = lock;
    }

    @Override
    public void run() {
        System.out.println(threadName+" is waiting for get lock ");
        synchronized (lock) {
            System.out.println(threadName+" get lock ");
            try {
                ThreadUtil.sleep(5000);
                System.out.println(threadName+" do wait start");
                lock.wait();
                System.out.println(threadName+" do wait end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
