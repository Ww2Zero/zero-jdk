package com.zero.test.java.util.arraylist;

import com.zero.test.util.ListUtil;

import java.util.ArrayList;

public class Test {

    public static void main(String[] args) throws IllegalAccessException {

        test01();
        test02();
        test03();
    }

    /**
     * list当前容量 = 10;list当前数据 = 0  // 初始化容量
     * list当前容量 = 23;list当前数据 = 0  // 扩容到指定容量
     * list当前容量 = 0;list当前数据 = 0   // 缩容到实际数据长度
     *
     * @throws IllegalAccessException
     */
    private static void test03() throws IllegalAccessException {
        ArrayList arrayList = new ArrayList(10);
        ListUtil.printfListCapacity(arrayList);
        arrayList.ensureCapacity(23);
        ListUtil.printfListCapacity(arrayList);
        arrayList.trimToSize();
        ListUtil.printfListCapacity(arrayList);
    }

    /**
     * 使用有参数构造函数，构造指定容量大小的数组
     * 当数据中元素数量添加到当前容量大小相等时进行扩容
     * 默认扩容规则为当前容量的1.5倍
     * <p>
     * ****
     * list当前容量 = 10;list当前数据 = 1
     * list当前容量 = 10;list当前数据 = 2
     * list当前容量 = 10;list当前数据 = 3
     * list当前容量 = 10;list当前数据 = 4
     * list当前容量 = 10;list当前数据 = 5
     * list当前容量 = 10;list当前数据 = 6
     * list当前容量 = 10;list当前数据 = 7
     * list当前容量 = 10;list当前数据 = 8
     * list当前容量 = 10;list当前数据 = 9
     * list当前容量 = 10;list当前数据 = 10
     * list当前容量 = 15;list当前数据 = 11
     * list当前容量 = 15;list当前数据 = 12
     * list当前容量 = 15;list当前数据 = 13
     * list当前容量 = 15;list当前数据 = 14
     * list当前容量 = 15;list当前数据 = 15
     * list当前容量 = 22;list当前数据 = 16
     * list当前容量 = 22;list当前数据 = 17
     * list当前容量 = 22;list当前数据 = 18
     * list当前容量 = 22;list当前数据 = 19
     * list当前容量 = 22;list当前数据 = 20
     * list当前容量 = 22;list当前数据 = 21
     * list当前容量 = 22;list当前数据 = 22
     * list当前容量 = 33;list当前数据 = 23
     * list当前容量 = 33;list当前数据 = 24
     * list当前容量 = 33;list当前数据 = 25
     * list当前容量 = 33;list当前数据 = 26
     * list当前容量 = 33;list当前数据 = 27
     * list当前容量 = 33;list当前数据 = 28
     * list当前容量 = 33;list当前数据 = 29
     * list当前容量 = 33;list当前数据 = 30
     * list当前容量 = 33;list当前数据 = 31
     * list当前容量 = 33;list当前数据 = 32
     * list当前容量 = 33;list当前数据 = 33
     * list当前容量 = 49;list当前数据 = 34
     * list当前容量 = 49;list当前数据 = 35
     * list当前容量 = 49;list当前数据 = 36
     * list当前容量 = 49;list当前数据 = 37
     * list当前容量 = 49;list当前数据 = 38
     * list当前容量 = 49;list当前数据 = 39
     * list当前容量 = 49;list当前数据 = 40
     * list当前容量 = 49;list当前数据 = 41
     * list当前容量 = 49;list当前数据 = 42
     * list当前容量 = 49;list当前数据 = 43
     * list当前容量 = 49;list当前数据 = 44
     * list当前容量 = 49;list当前数据 = 45
     * list当前容量 = 49;list当前数据 = 46
     * list当前容量 = 49;list当前数据 = 47
     * list当前容量 = 49;list当前数据 = 48
     * list当前容量 = 49;list当前数据 = 49
     * list当前容量 = 73;list当前数据 = 50
     * list当前容量 = 73;list当前数据 = 51
     * list当前容量 = 73;list当前数据 = 52
     * list当前容量 = 73;list当前数据 = 53
     * list当前容量 = 73;list当前数据 = 54
     * list当前容量 = 73;list当前数据 = 55
     * list当前容量 = 73;list当前数据 = 56
     * list当前容量 = 73;list当前数据 = 57
     * list当前容量 = 73;list当前数据 = 58
     * list当前容量 = 73;list当前数据 = 59
     * list当前容量 = 73;list当前数据 = 60
     * list当前容量 = 73;list当前数据 = 61
     * list当前容量 = 73;list当前数据 = 62
     * list当前容量 = 73;list当前数据 = 63
     * list当前容量 = 73;list当前数据 = 64
     * list当前容量 = 73;list当前数据 = 65
     * list当前容量 = 73;list当前数据 = 66
     * list当前容量 = 73;list当前数据 = 67
     * list当前容量 = 73;list当前数据 = 68
     * list当前容量 = 73;list当前数据 = 69
     * list当前容量 = 73;list当前数据 = 70
     * list当前容量 = 73;list当前数据 = 71
     * list当前容量 = 73;list当前数据 = 72
     * list当前容量 = 73;list当前数据 = 73
     * list当前容量 = 109;list当前数据 = 74
     * list当前容量 = 109;list当前数据 = 75
     * list当前容量 = 109;list当前数据 = 76
     * list当前容量 = 109;list当前数据 = 77
     * list当前容量 = 109;list当前数据 = 78
     * list当前容量 = 109;list当前数据 = 79
     * list当前容量 = 109;list当前数据 = 80
     * list当前容量 = 109;list当前数据 = 81
     * list当前容量 = 109;list当前数据 = 82
     * list当前容量 = 109;list当前数据 = 83
     * list当前容量 = 109;list当前数据 = 84
     * list当前容量 = 109;list当前数据 = 85
     * list当前容量 = 109;list当前数据 = 86
     * list当前容量 = 109;list当前数据 = 87
     * list当前容量 = 109;list当前数据 = 88
     * list当前容量 = 109;list当前数据 = 89
     * list当前容量 = 109;list当前数据 = 90
     * list当前容量 = 109;list当前数据 = 91
     * list当前容量 = 109;list当前数据 = 92
     * list当前容量 = 109;list当前数据 = 93
     * list当前容量 = 109;list当前数据 = 94
     * list当前容量 = 109;list当前数据 = 95
     * list当前容量 = 109;list当前数据 = 96
     * list当前容量 = 109;list当前数据 = 97
     * list当前容量 = 109;list当前数据 = 98
     * list当前容量 = 109;list当前数据 = 99
     * list当前容量 = 109;list当前数据 = 100
     *
     * @throws IllegalAccessException
     */
    private static void test02() throws IllegalAccessException {
        ArrayList<Integer> integers = new ArrayList<>(10);
        for (int i = 100; i > 0; i--) {
            integers.add(i);
            ListUtil.printfListCapacity(integers);
        }
    }

    /**
     * list的默认无参数构造方法，初始的容器为0
     * 添加第一个元素时进行扩容，当元素和当前容量相等时进行扩容
     * *****
     * list当前容量 = 0;list当前数据 = 0
     * list当前容量 = 10;list当前数据 = 1
     *
     * @throws IllegalAccessException
     */
    private static void test01() throws IllegalAccessException {
        ArrayList arrayList = new ArrayList<>();
        ListUtil.printfListCapacity(arrayList);
        arrayList.add(1);
        ListUtil.printfListCapacity(arrayList);
    }
}
