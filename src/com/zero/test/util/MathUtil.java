package com.zero.test.util;

public class MathUtil {

    private MathUtil() {
    }


    public static long random(int max) {
        double random = Math.random();
        return Math.round(random * max);
    }
}
