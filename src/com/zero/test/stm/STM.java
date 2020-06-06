package com.zero.test.stm;

public class STM {

    //提交数据需要⽤到的全局锁
    static final Object commitLock = new Object();

    //私有化构造⽅法
    private STM() {
    }

    //原⼦化提交⽅法
    public static boolean atomic(TxnRunnable action) {
        boolean committed = false;
        //如果没有提交成功，则⼀直重试
        while (!committed) {
            //创建新的事务
            STMTxn txn = new STMTxn();
            //执⾏业务逻辑
            boolean run = action.run(txn);
            if (run) {
                //提交事务
                committed = txn.commit();
            } else {
                return false;
            }

        }
        return true;
    }
}


