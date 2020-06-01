package com.zero.test.util;

import java.util.Objects;

public class TimeUtil {

    private long startTime;
    private String message;

    public TimeUtil() {
        startTime = System.currentTimeMillis();
    }

    public TimeUtil(String msg) {
        startTime = System.currentTimeMillis();
        message = msg;
    }

    public long time() {
        long endTime = System.currentTimeMillis();
        if (Objects.nonNull(message) && !message.isEmpty()) {
            System.out.printf("[%s]耗时：%d%n", message, endTime - startTime);
        } else {
            System.out.printf("耗时：%d%n", endTime - startTime);
        }
        return endTime - startTime;

    }

    public long time(String meg) {
        long endTime = System.currentTimeMillis();
        if (Objects.nonNull(message) && !message.isEmpty()) {
            System.out.printf("[%s]-%s耗时：%d%n", message, meg, endTime - startTime);
        } else {
            System.out.printf("%s耗时：%d%n", meg, endTime - startTime);
        }

        return endTime - startTime;

    }

    public void restart() {
        startTime = System.currentTimeMillis();
    }

    public void restart(String msg) {
        startTime = System.currentTimeMillis();
        message = msg;
    }
}
