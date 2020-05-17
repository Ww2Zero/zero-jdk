package com.zero.test.java.lang.throwable;


public class Test {

    public static void main(String[] args) {

        test01();
        test02();
        test04();
        test03();
    }

    private static void test04() {
        throw new RuntimeException("new runtimeException");
    }

    private static void test03() {
        throw new Error("new error");
    }

    private static void test02() {
        try {
            throw new Exception("new exception");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test01() {
        try {
            throw new Throwable("new Throwalbe");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
