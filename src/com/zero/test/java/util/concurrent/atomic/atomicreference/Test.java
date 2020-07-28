package com.zero.test.java.util.concurrent.atomic.atomicreference;

import com.zero.test.util.Assert;

import java.util.concurrent.atomic.AtomicReference;

public class Test {

    private static Student student9 = new Student("小明", 9);
    private static Student student10 = new Student("小明", 10);
    private static Student student11 = new Student("小明", 11);

    public static void main(String[] args) throws InterruptedException {

        test01();
        test02();
    }

    /**
     * A->B->A问题
     *
     * @throws InterruptedException
     */
    private static void test02() throws InterruptedException {
        AtomicReference<Student> atStudent = new AtomicReference<Student>(student9);
        Thread t = new Thread(() -> {
            atStudent.compareAndSet(student9, student11);
            Assert.isEquals(student11, atStudent.get());
            atStudent.compareAndSet(student11, student9);
            Assert.isEquals(student9, atStudent.get());
        });

        t.start();
        t.join();
        atStudent.compareAndSet(student9, student10);
        Assert.isEquals(student10, atStudent.get());
    }

    private static void test01() {
        AtomicReference<Student> atStudent = new AtomicReference<Student>(student9);
        atStudent.compareAndSet(student9, student10);
        Assert.isEquals(student10, atStudent.get());
    }
}
