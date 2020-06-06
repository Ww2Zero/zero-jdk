package com.zero.test.stm;

public class Account {

    private String name;
    //余额
    private TxnRef<Integer> balance;

    //构造⽅法
    public Account(String name, int balance) {
        this.name = name;
        this.balance = new TxnRef<>(balance);
    }

    //转账操作
    public void transfer(Account target, int amt) {
        STM.atomic((txn) -> {

            Integer from = balance.getValue(txn);
            if (amt > from) {
                return;
            }
            if (from == 0) {
                return;
            }
            balance.setValue(from - amt, txn);
            Integer to = target.balance.getValue(txn);
            target.balance.setValue(to + amt, txn);
        });
    }

    public Object getBalance() {
        return balance.getValue();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance=" + balance.getValue() +
                '}';
    }
}
