package com.zero.test.java.lang.thread.daemon;

import com.zero.test.util.ThreadUtil;

// 守护线程要么在自己的run方法执行完后结束，要么在其他所有线程都完成后自身也结束。
public class Test {
    public static void main(String[] args) {
        test01();
//        test02();
    }

    /**
     * 子线程设置为守护线程，main线程完成之后，子线程也随着结束运行
     *
     * Hello 0
     * Hello 1
     * Hello 2
     * ====main thread end ..
     * =+++++++++++++++++++++++++++++++++++++++++++++=
     */
    private static void test02() {
        Thread t = new Thread(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                while(true){
                    System.out.println("Hello "+count++);

                    ThreadUtil.sleep(2000);
                }
            }
        });
        t.setDaemon(true);
        t.start();
        ThreadUtil.sleep(5000);
        ThreadUtil.printLine("main thread end ..");
    }

    /**
     * main线程执行完成，但子线程仍然在运行中
     *
     * Hello 0
     * Hello 1
     * Hello 2
     * ====>main thread end ..
     * =+++++++++++++++++++++++++++++++++++++++++++++=
     * Hello 3
     * Hello 4
     * Hello 5
     *
     * Process finished with exit code 130 (interrupted by signal 2: SIGINT)
     */
    private static void test01() {
        Thread t = new Thread(new Runnable() {
            int count = 0;
            @Override
            public void run() {
                while(true){
                    System.out.println("Hello "+count++);

                    ThreadUtil.sleep(2000);
                }
            }
        });
        t.start();
        ThreadUtil.sleep(5000);
        ThreadUtil.printLine("main thread end ..");
    }
}
