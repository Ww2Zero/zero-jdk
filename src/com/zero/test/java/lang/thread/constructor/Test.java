package com.zero.test.java.lang.thread.constructor;

import com.zero.test.util.ThreadUtil;

public class Test {
    public static void main(String[] args) {
        test01();
        test02();
        test03();
        test04();
    }

    /**
     * 线程对象初始化
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
     * tg.getName() = main
     * tg.activeCount() = 2
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 0
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     * 调用线程的run方法    // 直接调用run方法并没有启动线程，而是在当前main线程中调用run方法
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[main,5,main]
     * t.getId() = 1
     * t.getName() = main
     * t.getState() = RUNNABLE
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * tg.getName() = main
     * tg.activeCount() = 2
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 5
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.getStackTrace(Thread.java:1559)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = getStackTrace
     * st.getLineNumber() = 1559
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.util.ThreadUtil.printThreadInfo(ThreadUtil.java:45)
     * st.getClassName() = com.zero.test.util.ThreadUtil
     * st.getFileName() = ThreadUtil.java
     * st.getMethodName() = printThreadInfo
     * st.getLineNumber() = 45
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.java.lang.thread.constructor.Test$1.run(Test.java:14)
     * st.getClassName() = com.zero.test.java.lang.thread.constructor.Test$1
     * st.getFileName() = Test.java
     * st.getMethodName() = run
     * st.getLineNumber() = 14
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.run(Thread.java:748)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = run
     * st.getLineNumber() = 748
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.java.lang.thread.constructor.Test.main(Test.java:21)
     * st.getClassName() = com.zero.test.java.lang.thread.constructor.Test
     * st.getFileName() = Test.java
     * st.getMethodName() = main
     * st.getLineNumber() = 21
     * st.isNativeMethod() = false
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     * 调用线程的start方法                // 第一次调用start()方法，在系统中创建实体线程进行相关任务处理
     * 再次调用线程的start方法             // 第二次调用start()方法，抛出java.lang.IllegalThreadStateException错误。
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
     * tg.getName() = main
     * tg.activeCount() = 3
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 4
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.getStackTrace(Thread.java:1559)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = getStackTrace
     * st.getLineNumber() = 1559
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.util.ThreadUtil.printThreadInfo(ThreadUtil.java:45)
     * st.getClassName() = com.zero.test.util.ThreadUtil
     * st.getFileName() = ThreadUtil.java
     * st.getMethodName() = printThreadInfo
     * st.getLineNumber() = 45
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.java.lang.thread.constructor.Test$1.run(Test.java:14)
     * st.getClassName() = com.zero.test.java.lang.thread.constructor.Test$1
     * st.getFileName() = Test.java
     * st.getMethodName() = run
     * st.getLineNumber() = 14
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.run(Thread.java:748)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = run
     * st.getLineNumber() = 748
     * st.isNativeMethod() = false
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     * Exception in thread "main" java.lang.IllegalThreadStateException
     * at java.lang.Thread.start(Thread.java:708)
     * at com.zero.test.java.lang.thread.constructor.Test.main(Test.java:25)
     */
    private static void test04() {
        Thread t4 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.printThreadInfo(Thread.currentThread());
                ThreadUtil.sleep(2000);
            }
        });
        System.out.println("线程对象初始化");
        ThreadUtil.printThreadInfo(t4);
        System.out.println("调用线程的run方法");
        t4.run();
        System.out.println("调用线程的start方法");
        t4.start();
        System.out.println("再次调用线程的start方法");
        t4.start();
    }

    /**
     * >=+++++++++++++++++++++++++++++++++++++++++++++=
     * t = Thread[Thread-0,5,zero]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = NEW
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=zero,maxpri=10]
     * t.isAlive() = false
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * tg.getName() = zero
     * tg.activeCount() = 0
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 0
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=zero,maxpri=10]
     */
    private static void test03() {
        ThreadGroup tg = new ThreadGroup("zero");
        Thread t3 = new Thread(tg, new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleep(2000);
            }
        });
        ThreadUtil.printThreadInfo(t3);
    }

    /**
     * >=+++++++++++++++++++++++++++++++++++++++++++++= 调用start之前
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
     * tg.getName() = main
     * tg.activeCount() = 2
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 0               // 没有调用栈帧信息，表示该线程对象并没有创建实际的线程
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     * >=+++++++++++++++++++++++++++++++++++++++++++++= 调用start之前
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0
     * t.getState() = TIMED_WAITING
     * t.getPriority() = 5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]
     * t.isAlive() = true               // 活跃线程标志 当线程状态不是 NEW或TERMINATED时，为活跃状态
     * t.isInterrupted() = false
     * t.isDaemon() = false
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * tg.getName() = main
     * tg.activeCount() = 3
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 4           // 有调用栈帧信息，表示该线程对象实际在操作系统创建了线程，并进行调用
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.sleep(Native Method)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = sleep
     * st.getLineNumber() = -2
     * st.isNativeMethod() = true
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.util.ThreadUtil.sleep(ThreadUtil.java:12)
     * st.getClassName() = com.zero.test.util.ThreadUtil
     * st.getFileName() = ThreadUtil.java
     * st.getMethodName() = sleep
     * st.getLineNumber() = 12
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = com.zero.test.java.lang.thread.constructor.Test$1.run(Test.java:97)
     * st.getClassName() = com.zero.test.java.lang.thread.constructor.Test$1
     * st.getFileName() = Test.java
     * st.getMethodName() = run
     * st.getLineNumber() = 97
     * st.isNativeMethod() = false
     * >>=+++++++++++++++++++++++++++++++++++++++++++++=
     * st = java.lang.Thread.run(Thread.java:748)
     * st.getClassName() = java.lang.Thread
     * st.getFileName() = Thread.java
     * st.getMethodName() = run
     * st.getLineNumber() = 748
     * st.isNativeMethod() = false
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     * <p>
     * Process finished with exit code 0
     */
    private static void test02() {
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleep(2000);
            }
        });
        ThreadUtil.printThreadInfo(t2);
        t2.start();
        ThreadUtil.printThreadInfo(t2);
    }


    /**
     * t = Thread[Thread-0,5,main]
     * t.getId() = 11
     * t.getName() = Thread-0  // 默认线程名称为"Thread-"+threadInitNumber， threadInitNumber从0计数
     * t.getState() = NEW   // 默认的线程状态为 NEW
     * t.getPriority() = 5  // 默认线程优先级为5
     * t.getThreadGroup() = java.lang.ThreadGroup[name=main,maxpri=10]  // 默认添加到当前线程所在的线程组中
     * t.isAlive() = false
     * t.isInterrupted() = false
     * t.isDaemon() = false // 默认不是守护线程
     * t.getContextClassLoader() = sun.misc.Launcher$AppClassLoader@18b4aac2
     * tg.getName() = main
     * tg.activeCount() = 2
     * tg.getMaxPriority() = 10
     * tg.activeGroupCount() = 0
     * tg.isDaemon() = false
     * tg.isDestroyed() = false
     * t.getStackTrace() = 0
     * t.getUncaughtExceptionHandler() = java.lang.ThreadGroup[name=main,maxpri=10]
     */
    private static void test01() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.sleep(2000);
            }
        });
        ThreadUtil.printThreadInfo(t1);
    }
}
