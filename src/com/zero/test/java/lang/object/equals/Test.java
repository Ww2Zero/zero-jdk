package com.zero.test.java.lang.object.equals;

public class Test {

    public static void main(String[] args) {

        testDefaultEquals();
        testOverrideEquals();
    }

    /**
     * 重写equals方法可以用来比较对象的内容
     */
    private static void testOverrideEquals() {
        Person02 p1 = new Person02("xiaoming", 6);
        Person02 p2 = new Person02("xiaoming", 6);
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p2==p2 " + (p2 == p1));
        System.out.println("p2.equals(p1) " + p2.equals(p1));
    }

    /**
     * 默认的equals的方法和"=="一样，比较对象地址（比较引用）
     */
    private static void testDefaultEquals() {
        Person01 p1 = new Person01("xiaoming", 6);
        Person01 p2 = new Person01("xiaoming", 6);
        System.out.println("p1 = " + p1);
        System.out.println("p2 = " + p2);
        System.out.println("p2==p2 " + (p2 == p1));
        System.out.println("p2.equals(p1) " + p2.equals(p1));
    }
}
