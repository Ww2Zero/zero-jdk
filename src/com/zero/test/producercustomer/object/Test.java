package com.zero.test.producercustomer.object;

import java.util.concurrent.atomic.AtomicInteger;

public class Test {


    public static void main(String[] args) {

        int max = 10;
        AtomicInteger inventory = new AtomicInteger(0);

        Object lock = new Object();

        new Thread(new Producer(lock, inventory, max)).start();
        new Thread(new Producer(lock, inventory, max)).start();
        new Thread(new Customer(lock, inventory)).start();
        new Thread(new Customer(lock, inventory)).start();


    }
}
