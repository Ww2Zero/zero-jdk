package com.zero.test.java.util.linkedlist;

import com.zero.test.util.ListUtil;
import com.zero.test.util.MathUtil;
import com.zero.test.util.TimeUtil;

import java.util.ArrayList;
import java.util.LinkedList;

public class Test {
    public static void main(String[] args) {
//        test01(100*10000);
//        test02();
        test03();

    }

    /**
     * LinkedList遍历时，不可以调用add()/remove()，否则ConcurrentModificationException
     * <p>
     * Exception in thread "main" java.util.ConcurrentModificationException
     * at java.util.LinkedList$ListItr.checkForComodification(LinkedList.java:966)
     * at java.util.LinkedList$ListItr.next(LinkedList.java:888)
     * at com.zero.test.java.util.linkedlist.Test.main(Test.java:19)
     */
    private static void test03() {
        LinkedList<Integer> linkedList = ListUtil.initLinkedList(10);
        for (Integer integer : linkedList) {
            linkedList.add(11);
        }
    }

    /**
     * [arrayList]-插入1000000数据耗时：55
     * [arrayList]-获取497728位置的数据耗时：0
     * [linkedlist]-插入1000000数据耗时：31
     * [linkedlist]-获取497728位置的数据耗时：4
     */
    private static void test02() {
        int num = 1000 * 1000;
        int index = (int) MathUtil.random(num);
        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        TimeUtil timeUtil = new TimeUtil("arrayList");
        for (int i = 0; i < num; i++) {
            arrayList.add(i);
        }
        timeUtil.time(String.format("插入%d数据", num));

        timeUtil.restart();
        arrayList.get(index);
        timeUtil.time(String.format("获取%d位置的数据", index));

        timeUtil.restart("linkedlist");
        for (int i = 0; i < num; i++) {
            linkedList.add(i);
        }
        timeUtil.time(String.format("插入%d数据", num));

        timeUtil.restart();
        linkedList.get(index);
        timeUtil.time(String.format("获取%d位置的数据", index));
    }

    /**
     * [arrayList]-插入1000000数据耗时：51
     * [linkedlist]-插入1000000数据耗时：31
     *
     * @param num
     */
    private static void test01(int num) {
        ArrayList<Integer> arrayList = new ArrayList<>();
        LinkedList<Integer> linkedList = new LinkedList<>();

        TimeUtil timeUtil = new TimeUtil("arrayList");
        for (int i = 0; i < num; i++) {
            arrayList.add(i);
        }
        timeUtil.time(String.format("插入%d数据", num));
        timeUtil.restart("linkedlist");
        long l = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            linkedList.add(i);
        }
        long l1 = System.currentTimeMillis();
        System.out.println("l1 = " + (l1 - l));
        timeUtil.time(String.format("插入%d数据", num));
    }
}
