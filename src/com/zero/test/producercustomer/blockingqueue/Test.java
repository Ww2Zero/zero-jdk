package com.zero.test.producercustomer.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Test {

    public static void main(String[] args) {

        BlockingQueue<String> strings = new ArrayBlockingQueue<String>(10);

        new Thread(new Producer(strings)).start();
        new Thread(new Producer(strings)).start();
        new Thread(new Customer(strings)).start();
    }
}
