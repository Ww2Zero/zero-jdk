package com.zero.test.stm;

public final class VersionedRef<T> {

    final T value;
    final long version;

    //构造⽅法
    public VersionedRef(T value, long version) {
        this.value = value;
        this.version = version;
    }
}
