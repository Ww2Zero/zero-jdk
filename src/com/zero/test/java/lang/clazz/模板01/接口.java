package com.zero.test.java.lang.clazz.模板01;

public interface 接口 {
    public          interface 接口_内部接口_public { }
    public abstract class     接口_内部抽象类_public { }
    public          class     接口_内部实例类_public { }

    // 接口字段都是public
    String 接口字段_public = "";

    void 接口方法_public();

    default void 接口方法_default() {
        // JDK 1.8 特性
    }

    static void 接口方法_static() {
        // JDK 1.8 特性
    }
}
