package com.zero.test.java.lang.thread.interrupt;

import com.zero.test.util.ThreadUtil;

public class Test {
    public static void main(String[] args) {
        test01();
        test02();
        test03();


    }

    /**
     * interrupt打断Object.wait进入的TIMEING_WAIT状态，解除阻塞
     *
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
     * java.lang.InterruptedException
     * 	at java.lang.Object.wait(Native Method)
     * 	at com.zero.test.util.ThreadUtil.waitObj(ThreadUtil.java:24)
     * 	at com.zero.test.java.lang.thread.interrupt.Test$1.run(Test.java:12)
     * 	at java.lang.Thread.run(Thread.java:748)
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
     *
     */
    private static void test03() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.printThreadShortInfo(Thread.currentThread());
                ThreadUtil.waitObj(80000);
                ThreadUtil.printThreadShortInfo(Thread.currentThread());
            }
        });

        t.start();
        ThreadUtil.sleep(1000);
        t.interrupt();
    }

    /**
     * interrupt打断线程调用sleep进入的TIMEING_WAIT状态，解除阻塞
     *
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
     * java.lang.InterruptedException: sleep interrupted
     * 	at java.lang.Thread.sleep(Native Method)
     * 	at com.zero.test.util.ThreadUtil.sleep(ThreadUtil.java:14)
     * 	at com.zero.test.java.lang.thread.interrupt.Test$1.run(Test.java:11)
     * 	at java.lang.Thread.run(Thread.java:748)
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
     *
     */
    private static void test02() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.printThreadShortInfo(Thread.currentThread());
                ThreadUtil.sleep(80000);
                ThreadUtil.printThreadShortInfo(Thread.currentThread());
            }
        });

        t.start();
        ThreadUtil.sleep(1000);
        t.interrupt();
    }

    /**
     * interrupt 方法只是修改中断标志位，并没有实际中断线程的运行
     * 线程处于阻塞状态时，只能收到中断通知，但无法设置中断位
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
     * t.getState() = RUNNABLE
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true
     * t.isInterrupted() = true
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     *
     * Process finished with exit code 130 (interrupted by signal 2: SIGINT)
     */
    private static void test01() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
               while(true){}
            }
        });
        ThreadUtil.printThreadShortInfo(t);
        t.start();
        ThreadUtil.printThreadShortInfo(t);
        t.interrupt();
        ThreadUtil.printThreadShortInfo(t);
    }
}
