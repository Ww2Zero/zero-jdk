package com.zero.test.jvm;

import java.util.ArrayList;
import java.util.List;
/**
 * VM Args:-Xmx10m -XX:+HeapDumpOnOutOfMemoryError
 */
public class OutOfMemoryError {
    /**
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     * 	at java.util.Arrays.copyOf(Arrays.java:3210)
     * 	at java.util.Arrays.copyOf(Arrays.java:3181)
     * 	at java.util.ArrayList.grow(ArrayList.java:265)
     * 	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:239)
     * 	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:231)
     * 	at java.util.ArrayList.add(ArrayList.java:462)
     * 	at com.zero.test.jvm.OutOfMemoryError.main(OutOfMemoryError.java:13)
     *
     * @param args
     */
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        while (true) {
            list.add("OutOfMemoryError soon");
        }
    }
}
