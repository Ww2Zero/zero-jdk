package com.zero.test.java.util.concurrent.atomic.atomicstampedreference;

import com.zero.test.java.util.concurrent.atomic.atomicreference.Student;
import com.zero.test.util.Assert;

import java.util.concurrent.atomic.AtomicStampedReference;

public class Test {

    private static Student student9 = new Student("小明", 9);
    private static Student student10 = new Student("小明", 10);
    private static Student student11 = new Student("小明", 11);

    public static void main(String[] args) throws InterruptedException {
        test01();
    }

    /**
     * A->B->A问题
     *
     * @throws InterruptedException
     */
    private static void test01() throws InterruptedException {
        AtomicStampedReference<Student> atStudent = new AtomicStampedReference<Student>(student9, 0);
        Thread t = new Thread(() -> {
            atStudent.compareAndSet(student9, student11, 0, 1);

            Assert.isEquals(student11, atStudent.getReference());
            atStudent.compareAndSet(student11, student9, 1, 2);
            Assert.isEquals(student9, atStudent.getReference());
        });

        t.start();
        t.join();
        atStudent.compareAndSet(student9, student10, 0, 1);
        Assert.isNotEquals(student10, atStudent.getReference());
    }
}
