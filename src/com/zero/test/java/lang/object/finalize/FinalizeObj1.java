package com.zero.test.java.lang.object.finalize;


public class FinalizeObj1 {

    @Override
    protected void finalize() throws Throwable {
        System.out.println("FinalizeObj call finalize method");
    }
}
