package com.zero.test.java.lang.enumm;

public class Test {

    public static void main(String[] args) {
        test01();
        test02();
        test03();
    }

    private static void test03() {
        Class<Color> declaringClass = Color.RED.getDeclaringClass();
        System.out.println("declaringClass = " + declaringClass);
    }

    private static void test02() {
        Color red = Enum.valueOf(Color.class, "RED");
        System.out.println("red = " + red);
    }

    private static void test01() {
        Color color = Color.RED;
        System.out.println("color = " + color);
    }

}
