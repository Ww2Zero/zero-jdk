package com.zero.test.producercustomer.lock;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    public static void main(String[] args) {
        int max = 10;
        AtomicInteger inventory = new AtomicInteger(0);
        ReentrantLock emptyLock = new ReentrantLock();
        ReentrantLock fullLock = new ReentrantLock();
        Condition emptyCon = emptyLock.newCondition();
        Condition fullCon = fullLock.newCondition();

        new Thread(new Producer(inventory, max, fullLock, fullCon, emptyLock, emptyCon)).start();
        new Thread(new Producer(inventory, max, fullLock, fullCon, emptyLock, emptyCon)).start();
        new Thread(new Producer(inventory, max, fullLock, fullCon, emptyLock, emptyCon)).start();
        new Thread(new Customer(inventory, max, emptyLock, emptyCon, fullLock, fullCon)).start();
        new Thread(new Customer(inventory, max, emptyLock, emptyCon, fullLock, fullCon)).start();
    }
}
