package com.zero.test.java.lang.thread.sleep;

import com.zero.test.util.ThreadUtil;

public class Test {

    public static void main(String[] args) {

        test01();

    }

    /**
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = NEW
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = false
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = RUNNABLE
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = TIMED_WAITING
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = RUNNABLE
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = TERMINATED
     * t.getPriority() = 5
     * t.getThreadGroup() = null
     * t.isAlive() = false
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     *
     */
    private static void test01() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ThreadUtil.printThreadShortInfo(Thread.currentThread());
                    Thread.sleep(2000);
                    ThreadUtil.printThreadShortInfo(Thread.currentThread());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ThreadUtil.printThreadShortInfo(t);
        t.start();
        ThreadUtil.sleep(1000);
        ThreadUtil.printThreadShortInfo(t);
        ThreadUtil.sleep(2000);
        ThreadUtil.printThreadShortInfo(t);
    }
}
