package com.zero.test.java.lang.object.finalize;


public class FinalizeObj2 {

    public static FinalizeObj2 fo2 = null;

    @Override
    protected void finalize() throws Throwable {
        System.out.println("FinalizeObj2 call finalize method");
        fo2 = this;
    }
}
