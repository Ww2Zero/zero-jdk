package com.zero.test.util;

public final class ClassUtil {

    private ClassUtil() {
    }

    public static void printClass(Class clazz){
        System.out.println("clazz.getName() = " + clazz.getName());
        System.out.println("clazz.getSimpleName() = " + clazz.getSimpleName());
        System.out.println("clazz.getCanonicalName() = " + clazz.getCanonicalName());
        System.out.println("clazz.getTypeName() = " + clazz.getTypeName());
    }

}