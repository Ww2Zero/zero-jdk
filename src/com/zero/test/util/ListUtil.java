package com.zero.test.util;

import java.lang.reflect.Field;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;

public class ListUtil {

    private ListUtil() {
    }

    public static ArrayList initArrayList() {
        return initArrayList(10);
    }

    public static ArrayList initArrayList(int num) {
        ArrayList<Double> integers = new ArrayList<>();
        for (int i = num; i > 0; i--) {
            integers.add(Math.random() * num);
        }
        return integers;
    }

    public static void printfList(List list) {
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i + " = " + list.get(i));
        }
    }

    public static void printfAllField(Object list) throws IllegalAccessException {
        Class<?> clazz = list.getClass();
        //向上循环  遍历父类
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field[] field = clazz.getDeclaredFields();
            for (Field f : field) {
                f.setAccessible(true);
                System.out.println("属性：" + f.getName() + " 值：" + f.get(list).toString());
            }
        }
    }

    public static void printfField(Object list, String fieldname) throws IllegalAccessException, NoSuchFieldException {
        Class<?> clazz = list.getClass();
        //向上循环  遍历父类
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field declaredField = clazz.getDeclaredField(fieldname);
            declaredField.setAccessible(true);
            System.out.println("属性：" + declaredField.getName() + " 值：" + declaredField.get(list).toString());
        }
    }

    public static void printfListCapacity(Object list) throws IllegalAccessException {
        Class<?> clazz = list.getClass();
        //向上循环  遍历父类
        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
            Field elementData = null;
            try {
                elementData = clazz.getDeclaredField("elementData");
                elementData.setAccessible(true);
                Object[] objects = (Object[]) elementData.get(list);
                System.out.printf("list当前容量 = %d;list当前数据 = %d%n", objects.length, ((AbstractCollection) list).size());
            } catch (NoSuchFieldException e) {
                return;
            }

        }
    }

}
