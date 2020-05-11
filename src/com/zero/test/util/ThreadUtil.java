package com.zero.test.util;

public final class ThreadUtil {

    public static final String LINE = "=+++++++++++++++++++++++++++++++++++++++++++++= ";

    private ThreadUtil() {
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printLine(int level) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < level; i++) {
            sb.append(">");
        }
        sb.append(LINE);
        System.out.println(sb.toString());
    }

    public static void printLine() {
        printLine(0);
    }

    public static void printThreadInfo(Thread t) {
        printLine(1);
        System.out.println("t = " + t);
        System.out.println("t.getId() = " + t.getId());
        System.out.println("t.getName() = " + t.getName());
        System.out.println("t.getState() = " + t.getState());
        System.out.println("t.getPriority() = " + t.getPriority());
        System.out.println("t.getThreadGroup() = " + t.getThreadGroup());
        System.out.println("t.isAlive() = " + t.isAlive());
        System.out.println("t.isInterrupted() = " + t.isInterrupted());
        System.out.println("t.isDaemon() = " + t.isDaemon());
        System.out.println("t.getContextClassLoader() = " + t.getContextClassLoader());
        printThreadGroupInfo(t.getThreadGroup());
        System.out.println("t.getStackTrace() = " + t.getStackTrace().length);
        for (StackTraceElement stackTraceElement : t.getStackTrace()) {
            printLine(2);
            printStackTraceInfo(stackTraceElement);
        }
        System.out.println("t.getUncaughtExceptionHandler() = " + t.getUncaughtExceptionHandler());
    }


    public static void printThreadGroupInfo(ThreadGroup tg) {
        System.out.println("tg.getName() = " + tg.getName());
        System.out.println("tg.activeCount() = " + tg.activeCount());
        System.out.println("tg.getMaxPriority() = " + tg.getMaxPriority());
        System.out.println("tg.activeGroupCount() = " + tg.activeGroupCount());
        System.out.println("tg.isDaemon() = " + tg.isDaemon());
        System.out.println("tg.isDestroyed() = " + tg.isDestroyed());
    }

    public static void printStackTraceInfo(StackTraceElement st) {
        System.out.println("st = " + st);
        System.out.println("st.getClassName() = " + st.getClassName());
        System.out.println("st.getFileName() = " + st.getFileName());
        System.out.println("st.getMethodName() = " + st.getMethodName());
        System.out.println("st.getLineNumber() = " + st.getLineNumber());
        System.out.println("st.isNativeMethod() = " + st.isNativeMethod());
    }

}
