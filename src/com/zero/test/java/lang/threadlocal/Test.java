package com.zero.test.java.lang.threadlocal;

import com.zero.test.util.ThreadUtil;

public class Test {
    public static void main(String[] args) {
//        test01();
//        test02();
//        test03();
        test04();
    }

    private static void test04() {
        ThreadLocal<Integer> local1 = new ThreadLocal<>();
        ThreadLocal<Integer> local2 = new ThreadLocal<>();
        local1.set(100);
        System.out.println("local.get() = " + local1.get());
        System.out.println("local.get() = " + local2.get());
        System.gc();
        System.out.println("local.get() = " + local1.get());
    }
    private static void test03() {
        ThreadLocal<Integer> local = new ThreadLocal<>();

        local.set(100);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Integer integer = local.get();
                System.out.println("integer 1= " + integer);
                local.set(111);
                integer = local.get();
                System.out.println("integer 2= " + integer);
            }
        }).start();
        ThreadUtil.sleep(2000);
        Integer integer = local.get();
        System.out.println("integer 3 = " + integer);
    }

    private static void test02() {
        ThreadLocal<Integer> local = new ThreadLocal<>();
        local.set(100);
        System.out.println("local.get() = " + local.get());
        System.gc();
        System.out.println("local.get() = " + local.get());
    }
    private static void test01() {
        ThreadLocal<Integer> local = new ThreadLocal<>();
        local.set(100);
        System.out.println("local.get() = " + local.get());
        local.remove();
        System.out.println("local.get() = " + local.get());
    }
}
