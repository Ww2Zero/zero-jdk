package com.zero.test.stm;

import java.util.Objects;

public class TxnRef<T> {
    //当前数据，带版本号
    volatile VersionedRef curRef;

    //构造⽅法
    public TxnRef(T value) {
        this.curRef = new VersionedRef(value, 0L);
    }

    //获取当前事务中的数据
    public T getValue(Txn txn) {
        return txn.get(this);
    }

    //在当前事务中设置数据
    public void setValue(T value, Txn txn) {
        txn.set(this, value);
    }

    public Object getValue() {
        return curRef.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TxnRef<?> txnRef = (TxnRef<?>) o;
        return Objects.equals(curRef, txnRef.curRef);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curRef);
    }
}
