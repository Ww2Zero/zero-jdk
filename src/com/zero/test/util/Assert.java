package com.zero.test.util;

import java.util.Objects;

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

    public static void isEquals(Object a, Object b) {
        if (!Objects.equals(a, b)) {
            throw new RuntimeException("check error");
        }
    }

    public static void isNotEquals(Object a, Object b) {
        if (Objects.equals(a, b)) {
            throw new RuntimeException("check error");
        }
    }
}
