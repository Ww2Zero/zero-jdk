package com.zero.test.java.lang.string.intern;

/**
 * 在 JDK1.6 中，它在方法区中，属于 “永久代”.
 * 在 JDK1.7 中，它被移除方法区，放在 java 堆中。
 * 在 JDK1.8 中，取消了 “永久代”，将常量池放在元空间，与堆独立。
 */
public class Test {

    public static void main(String[] args) {
        test01();
        test02();
        test03();
        test04();
        test05();

        test06();

    }

    private static void test06() {
        String a1 = "A";
        String a2 = "B";
        String a3 = a1 + a2; // 在堆上分配内存
        String a4 = "AB";
        System.out.println("a3==a4 = " + (a3 == a4)); // false
    }

    private static void test05() {
        String a1 = "AABB";
        String a2 = "AA" + new String("BB"); // 堆上创建2个对象 对象BB，对象AABB
        System.out.println("a1 == a2.intern() =" + (a1 == a2.intern()));//true
        System.out.println("a2 == a2.intern() =" + (a2 == a2.intern())); //false
    }

    private static void test04() {
        String a1 = new String("A") + new String("B"); // 创建3个对象，对象A ，对象B ，对象AB
        String a3 = "A" + "B";
        System.out.println("a1 == a3 " + (a1 == a3));  //false
    }

    /**
     * 判断这两个常量、相加后的常量在常量池上是否存在
     *   如果不存在
     *    则在常量池上创建相应的常量
     *   如果存在
     *    判断这个常量是存在的引用还是常量，
     *     如果是引用，返回引用地址指向的堆空间对象，
     *     如果是常量，则直接返回常量池常量，
     */
    private static void test03() {
        String b1 = "A" + "B";       // 在常量池中添加"A","B","AB"等三个常量
        String b2 = "AB";
        System.out.println("b1==b2 = " + (b1 == b2));
    }


    /**
     * 在堆上创建对象
     * <p>
     * 首先在堆上创建对象 (无论堆上是否存在相同字面量的对象),
     *  然后判断常量池上是否存在字符串的字面量，
     *   如果不存在
     *    在常量池上创建常量
     *   如果存在
     *    不做任何操作
     */
    private static void test02() {
        String b1 = new String("BB");           // 返回堆上的地址
        String b2 = new String("BB");           // 返回堆上的地址
        String b3 = new String("BB").intern();  // 返回常量池中引用
        String b4 = "BB";
        System.out.println("b1==b2 = " + (b1 == b2));  // false
        System.out.println("b2==b3 = " + (b2 == b3));  // false
        System.out.println("b4==b3 = " + (b4 == b3));  // true 都是常量池的引用

    }

    /**
     * 只在常量池上创建常量
     * <p>
     * 判断这个常量是否存在于常量池，
     *   如果存在，
     *    判断这个常量是存在的引用还是常量，
     *     如果是引用，返回引用地址指向的堆空间对象，
     *     如果是常量，则直接返回常量池常量，
     *   如果不存在，
     *     在常量池中创建该常量，并返回此常量
     */
    private static void test01() {
        String a1 = "AA";
        String a2 = "AA";
        System.out.println("a1==a2 = " + (a1 == a2));  // true  都是常量池中引用

        String a3 = new String("AAA");
        String a5 = a3.intern();
        String a4 = "AAA";
        System.out.println("a4==a3 = " + (a4 == a3));  // false a3为堆上引用，a4为常量池上引用
        System.out.println("a4==a5 = " + (a4 == a5));  // true  a5和a4 一样都是常量池上引用

    }
}
