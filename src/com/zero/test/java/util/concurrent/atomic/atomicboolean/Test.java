package com.zero.test.java.util.concurrent.atomic.atomicboolean;


import com.zero.test.util.Assert;
import com.zero.test.util.ThreadUtil;

import java.util.concurrent.atomic.AtomicBoolean;

public class Test {

    public static void main(String[] args) {
        test01();
    }

    private static void test01() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);

        Thread t1 = new Thread(() -> {
            while (atomicBoolean.get()) {
                System.out.println("atomicBoolean = " + atomicBoolean.get());
                ThreadUtil.sleep(1000);
            }
            System.out.println("atomicBoolean = " + atomicBoolean.get());
            System.out.println("bool状态修改，正在退出线程");
        });
        t1.start();

        ThreadUtil.sleep(5000);
        System.out.println("set atomicBoolean is false ");
        atomicBoolean.set(false);
        System.out.println("t1.isAlive() = " + t1.isAlive());
        ThreadUtil.sleep(5000);
        System.out.println("t1.isAlive() = " + t1.isAlive());
        Assert.isFalse(t1.isAlive());
    }
}
