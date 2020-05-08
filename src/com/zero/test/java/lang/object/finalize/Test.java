package com.zero.test.java.lang.object.finalize;

import com.zero.test.util.ThreadUtil;

public class Test {

    public static void main(String[] args) {
        testNull();
        testGC();
        testFinalizeCall();
    }

    /**
     * finalize() 方法最多只会被调用一次的特性,可以实现延长对象的生命周期
     * 运行结果：
     * true    --> 第一次GC调用finalize，引用赋值给自己，延长生命周期
     * false   --> 第二次GC不在调用finalize，故该对象被回收，所以返回false
     */
    private static void testFinalizeCall() {
        FinalizeObj2 fo = new FinalizeObj2();
        fo = null;
        System.out.println("gc 1");
        System.gc();
        ThreadUtil.sleep(1000);

        fo = FinalizeObj2.fo2;
        System.out.println(fo != null);

        fo = null;
        System.out.println("gc 2");
        System.gc();
        ThreadUtil.sleep(1000);
        System.out.println(fo != null);
    }


    /**
     * 测试系统GC时，调用finalize()
     * <p>
     * 但是触发系统GC的时刻不确定性，该方法的调用同样也有不确定性
     */
    private static void testGC() {
        FinalizeObj1 f = new FinalizeObj1();
        f = null;
        System.gc();
    }

    /**
     * 测试将对象赋值为null时，不调用finalize()
     */
    private static void testNull() {
        FinalizeObj1 f = new FinalizeObj1();
        f = null;
    }
}
