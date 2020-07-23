package com.zero.test.util;

public class Assert {

    private Assert() {
    }


    public static void isTrue(boolean b) {
        isFalse(!b);
    }


    public static void isFalse(boolean b) {
        if (b) {
            throw new RuntimeException("check error");
        }
    }
}
