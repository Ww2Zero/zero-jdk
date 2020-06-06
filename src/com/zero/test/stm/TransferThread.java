package com.zero.test.stm;

import java.util.concurrent.atomic.AtomicInteger;

public class TransferThread extends Thread {

    private Account from;
    private Account to;
    private Integer balance;
    private AtomicInteger time;

    public TransferThread(String name, Account from, Account to, Integer balance) {
        super(name);
        this.from = from;
        this.to = to;
        this.balance = balance;
        time = new AtomicInteger();
    }

    @Override
    public void run() {
        transfer(from, to, balance);
    }

    public void transfer(Account from, Account to, Integer balance) {
        while (true) {
            if (0 >= (Integer) from.getBalance()) {
                return;
            }
            if (0 >= ((Integer) from.getBalance() - balance)) {
                return;
            }
            int i = time.incrementAndGet();
            System.out.printf("thread-[%s]-第%d转账[%s-->%s]=[%d]RMB%n", Thread.currentThread().getName(), i, from.getName(), to.getName(), balance);
            from.transfer(to, balance);
            System.out.printf("thread-[%s][%d]-%s%n", Thread.currentThread().getName(), i, from.toString());
            System.out.printf("thread-[%s][%d]-%s%n", Thread.currentThread().getName(), i, to.toString());

        }
    }

}
