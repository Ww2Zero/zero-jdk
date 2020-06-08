package com.zero.test.stm;

import java.util.Objects;

public final class VersionedRef<T> {

    final T value;
    final long version;

    //构造⽅法
    public VersionedRef(T value, long version) {
        this.value = value;
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VersionedRef<?> that = (VersionedRef<?>) o;
        return version == that.version &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, version);
    }
}
