package com.zero.test.java.lang.object.hashcode;

import com.zero.test.java.lang.object.equals.Person01;

import java.util.HashSet;

public class Test {

    public static void main(String[] args) {
//        testDefaultHashCode();
//        testOverrideHashCode();
        testHashSet();

    }

    /**
     * set1的size为4，说明默认的hashcode和equals方法，会将相同内容的对象添加到HashSet中
     * set2的size为4，说明重写hashcode方法和默认的equals方法（不重写），存放在HashSet时当将相同内容多个的对象当作不同的对象来处理
     * set3的size为3，说明重写hashcode和equals方法，HashSet会将相同内容的多个对象当做同一对象处理
     * <p>
     * 通过对比得知，当对象要存放在HashSet类似的结构中时，需要同时重写hashcode和equals方法，才可以在存入对象时正确的判断对象是否重复
     */
    private static void testHashSet() {
        HashSet set1 = new HashSet();
        HashSet set2 = new HashSet();
        HashSet set3 = new HashSet();
        Person01 p1 = new Person01("jack", 12);
        Person01 p2 = new Person01("polo", 13);
        Person01 p3 = new Person01("lucy", 12);
        Person01 p4 = new Person01("jack", 12);
        set1.add(p1);
        set1.add(p2);
        set1.add(p3);
        set1.add(p4);
        System.out.println("set.size() = " + set1.size());

        Person03 p5 = new Person03("jack", 12);
        Person03 p6 = new Person03("polo", 13);
        Person03 p7 = new Person03("lucy", 12);
        Person03 p8 = new Person03("jack", 12);
        System.out.println("p5 = " + p5.hashCode());
        System.out.println("p8 = " + p8.hashCode());
        System.out.println("p5.equals(p8) = " + p5.equals(p8));
        set2.add(p5);
        set2.add(p6);
        set2.add(p7);
        set2.add(p8);
        System.out.println("set.size() = " + set2.size());

        Person04 p9 = new Person04("jack", 12);
        Person04 p10 = new Person04("jack", 12);
        Person04 p11 = new Person04("polo", 13);
        Person04 p12 = new Person04("lucy", 12);
        System.out.println("p9 = " + p9.hashCode());
        System.out.println("p10= " + p10.hashCode());
        System.out.println("p9.equals(p10) = " + p9.equals(p10));
        set3.add(p9);
        set3.add(p10);
        set3.add(p11);
        set3.add(p12);
        System.out.println("set.size() = " + set3.size());
    }

    /**
     * 重写的hashcode方法，可以使相同的内容的对象返回相同的hashcode
     * 便于存储在hashMap中对判断重复的对象
     */
    private static void testOverrideHashCode() {
        Person03 p1 = new Person03("jack", 12);
        Person03 p2 = new Person03("jack", 12);
        System.out.println("p1.hashCode() = " + p1.hashCode());
        System.out.println("p2.hashCode() = " + p2.hashCode());
        System.out.println("p1.hashCode()==p2.hashCode() " + (p1.hashCode() == p2.hashCode()));
    }


    /**
     * 默认的hashcode方法，每个对象都生成不同的hashcode(即使对象内容相同)
     */
    private static void testDefaultHashCode() {
        Person01 p1 = new Person01("jack", 12);
        Person01 p2 = new Person01("jack", 12);
        System.out.println("p1.hashCode() = " + p1.hashCode());
        System.out.println("p2.hashCode() = " + p2.hashCode());
        System.out.println("p1.hashCode()==p2.hashCode() " + (p1.hashCode() == p2.hashCode()));
    }

}
