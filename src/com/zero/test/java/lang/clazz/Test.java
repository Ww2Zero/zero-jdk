package com.zero.test.java.lang.clazz;

import com.zero.test.util.ClassUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException {


//        test01();
//        test02();
//        test03();
//        test04();

//        test05();

//        test06();

//        test07();
//        test08();

        test09();
    }

    /**
     * class com.zero.test.java.lang.clazz.Test
     * class com.zero.test.java.lang.clazz.Test
     * null
     *
     * class com.zero.test.java.lang.clazz.Test
     * null
     * null
     * null
     */
    private static void test09() {
        class 方法内部类{
        }

        接口07 匿名内部类对象 = new 接口07() {
        };

        // 测试getEnclosingClass()
        System.out.println(成员内部类.class.getEnclosingClass());
        System.out.println(方法内部类.class.getEnclosingClass());
        System.out.println(匿名内部类对象.getClass().getEnclosingClass()); // 匿名内部类
        System.out.println(接口07.class.getEnclosingClass());   // 外部类

        System.out.println();

        // 测试getDeclaringClass()
        System.out.println(成员内部类.class.getDeclaringClass());
        System.out.println(方法内部类.class.getDeclaringClass());
        System.out.println(匿名内部类对象.getClass().getDeclaringClass()); // 匿名内部类
        System.out.println(接口07.class.getDeclaringClass());   // 外部类
    }

    class 成员内部类{
    }
    /**
     * 父类
     * class java.util.AbstractSet
     * 父接口
     * interface java.util.Set
     * interface java.lang.Cloneable
     * interface java.io.Serializable
     */
    private static void test08() {
        // 父类
        System.out.println(HashSet.class.getSuperclass());

        System.out.println();

        // 父接口
        Class[] cs = HashSet.class.getInterfaces();
        for(Class c : cs){
            System.out.println(c);
        }
    }


    /**
     * null
     * int
     * class [I
     *
     * null
     * class java.lang.Integer
     * class [Ljava.lang.Integer;
     */
    private static void test07() {
        System.out.println(int.class.getComponentType());
        System.out.println(int[].class.getComponentType());
        System.out.println(int[][].class.getComponentType());

        System.out.println();

        System.out.println(Integer.class.getComponentType());
        System.out.println(Integer[].class.getComponentType());
        System.out.println(Integer[][].class.getComponentType());
    }

    /**
     * clazz.getName() = [I
     * clazz.getSimpleName() = int[]
     * clazz.getCanonicalName() = int[]
     * clazz.getTypeName() = int[]
     *
     * clazz.getName() = [[I
     * clazz.getSimpleName() = int[][]
     * clazz.getCanonicalName() = int[][]
     * clazz.getTypeName() = int[][]
     *
     * clazz.getName() = [Ljava.lang.Integer;
     * clazz.getSimpleName() = Integer[]
     * clazz.getCanonicalName() = java.lang.Integer[]
     * clazz.getTypeName() = java.lang.Integer[]
     *
     * clazz.getName() = [[Ljava.lang.Integer;
     * clazz.getSimpleName() = Integer[][]
     * clazz.getCanonicalName() = java.lang.Integer[][]
     * clazz.getTypeName() = java.lang.Integer[][]
     */
    private static void test06() {
        ClassUtil.printClass(int[].class);
        System.out.println();
        ClassUtil.printClass(int[][].class);
        System.out.println();
        ClassUtil.printClass(Integer[].class);
        System.out.println();
        ClassUtil.printClass(Integer[][].class);
    }

    /**
     * =================普通类=================
     * clazz.getName() = com.zero.test.java.lang.clazz.Test
     * clazz.getSimpleName() = Test
     * clazz.getCanonicalName() = com.zero.test.java.lang.clazz.Test
     * clazz.getTypeName() = com.zero.test.java.lang.clazz.Test
     *
     * =================内部类=================
     * clazz.getName() = com.zero.test.java.lang.clazz.Test$innerClass
     * clazz.getSimpleName() = innerClass
     * clazz.getCanonicalName() = com.zero.test.java.lang.clazz.Test.innerClass
     * clazz.getTypeName() = com.zero.test.java.lang.clazz.Test$innerClass
     *
     * =================匿名类=================
     * clazz.getName() = com.zero.test.java.lang.clazz.Test$1
     * clazz.getSimpleName() =
     * clazz.getCanonicalName() = null
     * clazz.getTypeName() = com.zero.test.java.lang.clazz.Test$1
     */
    private static void test05() {
        System.out.println("\n=================普通类=================");
        ClassUtil.printClass(Test.class);

        System.out.println("\n=================内部类=================");
        ClassUtil.printClass(innerClass.class);

        System.out.println("\n=================匿名类=================");
        zinterface zObject = new zinterface() {
            @Override
            public void run() {
                ClassUtil.printClass(this.getClass());
            }
        };
        zObject.run();
    }

    /**
     * 内部类
     */
    class innerClass {
    }

    /**
     * 基本数据类型的
     * int
     * int
     * int
     * int
     */
    private static void test04() {
        System.out.println(int.class.getName());
        System.out.println(int.class.getSimpleName());
        System.out.println(int.class.getCanonicalName());
        System.out.println(int.class.getTypeName());
    }

    /**
     * 加载类，并设置是否初始化的flag
     * <p>
     * ====================
     * -->静态初始化（只执行一次）<--
     * -->无参数构造方法<--
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void test03() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.zero.test.java.lang.clazz.A", false, ClassLoader.getSystemClassLoader());
        System.out.println(" ==================== ");
        Object o = aClass.newInstance();
    }

    /**
     * 加载类，并调用静态方法块
     * <p>
     * -->静态初始化（只执行一次）<--     //加载类的时候，调用静态代码块
     * ====================
     * -->无参数构造方法<--
     * -->无参数构造方法<--
     * ====================
     * -->无参数构造方法<--
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private static void test02() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> aClass = Class.forName("com.zero.test.java.lang.clazz.A");
        System.out.println(" ==================== ");
        Object o = aClass.newInstance();
        Object o1 = aClass.newInstance();
        Class<?> bClass = Class.forName("com.zero.test.java.lang.clazz.A");
        System.out.println(" ==================== ");
        Object o2 = bClass.newInstance();

    }


    private static void test01() {
        Class<A> aClass = A.class;
        System.out.println("aClass.getName() = " + aClass.getName());
        System.out.println("aClass.getClass() = " + aClass.getClass());
        System.out.println("aClass.getClassLoader() = " + aClass.getClassLoader());
        System.out.println("aClass.getSimpleName() = " + aClass.getSimpleName());
        System.out.println("aClass.getTypeName() = " + aClass.getTypeName());
        System.out.println("aClass.getCanonicalName() = " + aClass.getCanonicalName());
        System.out.println("aClass.getSuperclass() = " + aClass.getSuperclass());
        System.out.println("aClass.getComponentType() = " + aClass.getComponentType());
        System.out.println("aClass.getModifiers() = " + aClass.getModifiers());
        System.out.println("aClass.getPackage() = " + aClass.getPackage());

        Method[] methods = aClass.getMethods();
        for (Method method : methods) {
            System.out.println("method = " + method);
        }
        Method[] declaredMethods = aClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            System.out.println("declaredMethod = " + declaredMethod);
        }

        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            System.out.println("field = " + field);
        }

        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            System.out.println("declaredField = " + declaredField);
        }
    }
}

interface zinterface {
    void run();
}
interface 接口07 {
}
