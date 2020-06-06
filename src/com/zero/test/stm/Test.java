package com.zero.test.stm;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Account a1 = new Account("小明", 500);
        Account a2 = new Account("小红", 500);
        Account a3 = new Account("小花", 500);

        TransferThread dd = new TransferThread("dd", a1, a2, -500);
        TransferThread ff = new TransferThread("ff", a2, a3, 880);
        System.out.println(" ====转账开始====");
        dd.start();
        ff.start();
        dd.join();
        ff.join();
        System.out.println(" ====转账结束====");
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
    }
}
