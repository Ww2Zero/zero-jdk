package com.zero.test.java.lang.clazz;

import com.zero.test.java.lang.clazz.模板01.A;
import com.zero.test.java.lang.clazz.模板01.实例类;
import com.zero.test.java.lang.clazz.模板02.子类;
import com.zero.test.java.lang.clazz.模板03.Child;
import com.zero.test.java.lang.clazz.模板03.Parent;
import com.zero.test.java.lang.clazz.模板03.注解_不可继承_public;
import com.zero.test.java.lang.clazz.模板03.注解_可继承_public;
import com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承;
import com.zero.test.java.lang.clazz.模板04.可重复注解_可继承;
import com.zero.test.util.ClassUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.HashSet;

public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException, NoSuchMethodException {


//        test01();
//        test02();
//        test03();
//        test04();

//        test05();

//        test06();

//        test07();
//        test08();

//        test09();

//        test10();
//        test11();


//        test12();
//        test13();
//        test14();
//        test15();

//        test16();

//        test17();

//        test18();
//        test19();


//        test20();
//        test21();


//        test22();

//        test23();
//        test24();

        test25();

    }

    /**
     * ====getAnnotatedInterfaces（要求注解支持ElementType.TYPE_USE）====
     *
     * ====获取Annotation====
     * @com.zero.test.java.lang.clazz.模板05.注解03()
     * @com.zero.test.java.lang.clazz.模板05.注解04()
     *
     * ====获取Type====
     * interface com.zero.test.java.lang.clazz.模板05.Interface01
     * interface com.zero.test.java.lang.clazz.模板05.Interface02
     */
    private static void test25() {
        System.out.println("====getAnnotatedInterfaces（要求注解支持ElementType.TYPE_USE）====");

        // 这里的ats代表“@注解01 Interface01, @注解02 Interface02”这个整体
        AnnotatedType[] ats = com.zero.test.java.lang.clazz.模板05.Child.class.getAnnotatedInterfaces();

        System.out.println("\n====获取Annotation====");
        for(AnnotatedType at : ats){
            Annotation[] annotations =at.getAnnotations();
            for (Annotation a : annotations){
                System.out.println(a);
            }
        }

        System.out.println("\n====获取Type====");
        for(AnnotatedType at : ats){
            Type type = at.getType();
            System.out.println(type);
        }
    }

    /**
     * ====getAnnotatedSuperclass（要求注解支持ElementType.TYPE_USE）====
     *
     * ====获取Annotation====
     * @com.zero.test.java.lang.clazz.模板05.注解01()
     * @com.zero.test.java.lang.clazz.模板05.注解02()
     *
     * ====获取Type====
     * class com.zero.test.java.lang.clazz.模板05.Parent
     * false
     */
    private static void test24() {
        System.out.println("====getAnnotatedSuperclass（要求注解支持ElementType.TYPE_USE）====");

        // 这里的at代表“@注解01 @注解02 Parent”这个整体，而不单是Parent
        AnnotatedType at = com.zero.test.java.lang.clazz.模板05.Child.class.getAnnotatedSuperclass();

        System.out.println("\n====获取Annotation====");
        Annotation[] annotations =at.getAnnotations();
        for (Annotation a : annotations){
            System.out.println(a);
        }

        System.out.println("\n====获取Type====");
        Type type = at.getType();
        System.out.println(type);
        System.out.println(type== Parent.class);
    }

    /**
     * ====getDeclaredAnnotationsByType====
     *
     * ====父类中指定类型的注解（不包括继承而来的注解）====
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=父类-1)
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=父类-2)
     *
     * ====子类中指定类型的注解（不包括继承而来的注解）====
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=子类-1)
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=子类-2)
     *
     */
    private static void test23() {
        System.out.println("====getDeclaredAnnotationsByType====");

        System.out.println("\n====父类中指定类型的注解（不包括继承而来的注解）====");
        Annotation[] as1 = com.zero.test.java.lang.clazz.模板04.Parent.class.getDeclaredAnnotationsByType(可重复注解_不可继承.class);
        for (Annotation a : as1){
            System.out.println(a);
        }


        System.out.println("\n====子类中指定类型的注解（不包括继承而来的注解）====");
        Annotation[] as2 = com.zero.test.java.lang.clazz.模板04.Child.class.getDeclaredAnnotationsByType(可重复注解_不可继承.class);
        for (Annotation a : as2){
            System.out.println(a);
        }

        System.out.println();

        Annotation[] as3 = com.zero.test.java.lang.clazz.模板04.Child.class.getDeclaredAnnotationsByType(可重复注解_可继承.class);
        for (Annotation a : as3){
            System.out.println(a);  // 无输出，因为没有获取到相应的注解
        }
    }

    /**
     * ====getAnnotationsByType====  // 获取指定的注解的数组
     *
     * ====父类中指定类型的注解（包括继承而来的注解）====
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=父类-1)
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=父类-2)
     *
     * ====子类中指定类型的注解（包括继承而来的注解）====
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=子类-1)
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_不可继承(str=子类-2)
     *
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_可继承(str=父类-3) // 继承父类
     * @com.zero.test.java.lang.clazz.模板04.可重复注解_可继承(str=父类-4) // 继承父类
     */
    private static void test22() {
        System.out.println("====getAnnotationsByType====");

        System.out.println("\n====父类中指定类型的注解（包括继承而来的注解）====");
        Annotation[] as1 = com.zero.test.java.lang.clazz.模板04.Parent.class.getAnnotationsByType(可重复注解_不可继承.class);
        for (Annotation a : as1){
            System.out.println(a);
        }

        System.out.println("\n====子类中指定类型的注解（包括继承而来的注解）====");
        Annotation[] as2 = com.zero.test.java.lang.clazz.模板04.Child.class.getAnnotationsByType(可重复注解_不可继承.class);
        for (Annotation a : as2){
            System.out.println(a);
        }

        System.out.println();

        Annotation[] as3 = com.zero.test.java.lang.clazz.模板04.Child.class.getAnnotationsByType(可重复注解_可继承.class);
        for (Annotation a : as3){
            System.out.println(a);
        }
    }

    /**
     * ====getDeclaredAnnotation====
     *
     * ====父类中指定类型的注解（不包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=父类中不可继承的注解)
     *
     * ====子类中指定类型的注解（不包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=子类中不可继承的注解)
     * null   // 当获取不到指定的注解时返回null
     */
    private static void test21() {
        System.out.println("====getDeclaredAnnotation====");

        System.out.println("\n====父类中指定类型的注解（不包括继承来的注解）====");
        Annotation a1 = Parent.class.getDeclaredAnnotation(注解_不可继承_public.class);
        System.out.println(a1);

        System.out.println("\n====子类中指定类型的注解（不包括继承来的注解）====");
        Annotation a2 = Child.class.getDeclaredAnnotation(注解_不可继承_public.class);
        Annotation a3 = Child.class.getDeclaredAnnotation(注解_可继承_public.class);
        System.out.println(a2);
        System.out.println(a3);
    }

    /**
     * ====getDeclaredAnnotations====
     *
     * ====父类中所有注解（不包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=父类中不可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_default(value=父类中不可继承的注解，且只允许在当前包内使用)
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_public(value=父类中可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_default(value=父类中可继承的注解，且只允许在当前包内使用)
     *
     * ====子类中所有注解（不包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=子类中不可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_default(value=子类中不可继承的注解，且只允许在当前包内使用)
     */
    private static void test20() {
        System.out.println("====getDeclaredAnnotations====");

        System.out.println("\n====父类中所有注解（不包括继承来的注解）====");
        Annotation[] as1 = Parent.class.getDeclaredAnnotations();
        for (Annotation a : as1){
            System.out.println(a);
        }

        System.out.println("\n====子类中所有注解（不包括继承来的注解）====");
        Annotation[] as2 = Child.class.getDeclaredAnnotations();
        for (Annotation a : as2){
            System.out.println(a);
        }
    }


    /**
     * ====getAnnotation====
     *
     * ====父类中指定类型的注解（包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=父类中不可继承的注解)
     *
     * ====子类中指定类型的注解（包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=子类中不可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_public(value=父类中可继承的注解)
     */
    private static void test19() {
        System.out.println("====getAnnotation====");

        System.out.println("\n====父类中指定类型的注解（包括继承来的注解）====");
        Annotation a1 = Parent.class.getAnnotation(注解_不可继承_public.class);
        System.out.println(a1);

        System.out.println("\n====子类中指定类型的注解（包括继承来的注解）====");
        Annotation a2 = Child.class.getAnnotation(注解_不可继承_public.class);
        Annotation a3 = Child.class.getAnnotation(注解_可继承_public.class);
        System.out.println(a2);
        System.out.println(a3);
    }

    /**
     * ====getAnnotations====
     *
     * ====父类中所有注解（包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=父类中不可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_default(value=父类中不可继承的注解，且只允许在当前包内使用)
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_public(value=父类中可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_default(value=父类中可继承的注解，且只允许在当前包内使用)
     *
     * ====子类中所有注解（包括继承来的注解）====
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_public(value=父类中可继承的注解)  // 继承父类
     * @com.zero.test.java.lang.clazz.模板03.注解_可继承_default(value=父类中可继承的注解，且只允许在当前包内使用) // 继承父类
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_public(value=子类中不可继承的注解)
     * @com.zero.test.java.lang.clazz.模板03.注解_不可继承_default(value=子类中不可继承的注解，且只允许在当前包内使用)
     */
    private static void test18() {
        System.out.println("====getAnnotations====");

        System.out.println("\n====父类中所有注解（包括继承来的注解）====");
        Annotation[] as1 = Parent.class.getAnnotations();
        for (Annotation a : as1){
            System.out.println(a);
        }

        System.out.println("\n====子类中所有注解（包括继承来的注解）====");
        Annotation[] as2 = Child.class.getAnnotations();
        for (Annotation a : as2){
            System.out.println(a);
        }
    }

    /**
     * ====getDeclaredConstructors====
     * private com.zero.test.java.lang.clazz.模板02.子类(int,int,int)
     * com.zero.test.java.lang.clazz.模板02.子类(int,int)
     * protected com.zero.test.java.lang.clazz.模板02.子类(int)
     * public com.zero.test.java.lang.clazz.模板02.子类()
     *
     * ====getDeclaredConstructor====
     * public com.zero.test.java.lang.clazz.模板02.子类()
     *
     * @throws NoSuchMethodException
     */
    private static void test17() throws NoSuchMethodException {
        System.out.println("\n====getDeclaredConstructors====");
        Constructor[] constructors = 子类.class.getDeclaredConstructors();
        for (Constructor c : constructors){
            System.out.println(c);
        }

        System.out.println("\n====getDeclaredConstructor====");
        Constructor constructor = 子类.class.getDeclaredConstructor();
        System.out.println(constructor);
    }

    /**
     * ====getConstructors====
     * public com.zero.test.java.lang.clazz.模板02.子类()
     *
     * ====getConstructor====
     * public com.zero.test.java.lang.clazz.模板02.子类()
     *
     * @throws NoSuchMethodException
     */
    private static void test16() throws NoSuchMethodException {
        System.out.println("\n====getConstructors====");
        Constructor[] constructors = 子类.class.getConstructors();
        for (Constructor c : constructors){
            System.out.println(c);
        }

        System.out.println("\n====getConstructor====");
        Constructor constructor = 子类.class.getConstructor();
        System.out.println(constructor);
    }

    /**
     * ====getDeclaredMethods====
     * 实例类方法_private
     * 实例类方法_public
     * 接口方法_public
     * 抽象类抽象方法_public
     * 抽象类抽象方法_protected
     * 抽象类抽象方法_default
     * 实例类方法_protected
     * 实例类方法_default
     *
     * ====getDeclaredMethod====
     * 实例类方法_private
     *
     * @throws NoSuchMethodException
     */
    private static void test15() throws NoSuchMethodException {
        // 返回当前类中所有public方法，包括父类/父接口中的public方法
        System.out.println("\n====getDeclaredMethods====");
        Method[] methods = 实例类.class.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        // 返回当前类中指定名称和形参的public方法，包括父类/父接口中的public方法
        System.out.println("\n====getDeclaredMethod====");
        Method method1 = 实例类.class.getDeclaredMethod("实例类方法_private");
        System.out.println(method1.getName());
    }

    /**
     * ====getMethods====
     * 实例类方法_public
     * 接口方法_public
     * 抽象类抽象方法_public
     * 抽象类非抽象方法_public
     * wait
     * wait
     * wait
     * equals
     * toString
     * hashCode
     * getClass
     * notify
     * notifyAll
     * 接口方法_default
     *
     * ====getMethod====
     * 实例类方法_public
     * Exception in thread "main" java.lang.NoSuchMethodException: com.zero.test.java.lang.clazz.模板01.实例类.实例类方法_无()
     * 	at java.lang.Class.getMethod(Class.java:1786)
     * 	at com.zero.test.java.lang.clazz.Test.main(Test.java:48)
     * @throws NoSuchMethodException
     */
    private static void test14() throws NoSuchMethodException {
        // 返回当前类中所有public方法，包括父类/父接口中的public方法
        System.out.println("\n====getMethods====");
        Method[] methods = 实例类.class.getMethods();
        for (Method method : methods) {
            System.out.println(method.getName());
        }

        // 返回当前类中指定名称和形参的public方法，包括父类/父接口中的public方法
        System.out.println("\n====getMethod====");
        Method method1 = 实例类.class.getMethod("实例类方法_public");
        System.out.println(method1.getName());
        Method method2 = 实例类.class.getMethod("实例类方法_无");
        System.out.println(method2.getName());
    }

    /**
     * ====getDeclaredFields====
     * 实例类字段_public
     * 实例类字段_protected
     * 实例类字段_default
     * 实例类字段_private
     *
     * ====getDeclaredField====
     * 实例类字段_private
     * @throws NoSuchFieldException
     */
    private static void test13() throws NoSuchFieldException {
        // 返回当前类中所有字段，但不包括父类/父接口中的字段
        System.out.println("\n====getDeclaredFields====");
        Field[] fields = 实例类.class.getDeclaredFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        // 返回当前类中指定名称的字段，但不包括父类/父接口中的字段
        System.out.println("\n====getDeclaredField====");
        Field field = 实例类.class.getDeclaredField("实例类字段_private");
        System.out.println(field.getName());
    }

    /**
     * ====getFields====  // 获取所有的public的字段，包含本类和父类和实现的接口中的public的字段
     * 实例类字段_public
     * 抽象类字段_public
     * 接口字段_public
     *
     * ====getField====  // 获取本类或父类或实现的接口中指定的字段
     * 实例类字段_public
     * 抽象类字段_public
     * 接口字段_public
     * Exception in thread "main" java.lang.NoSuchFieldException: 接口字段_无
     * 	at java.lang.Class.getField(Class.java:1703)
     * 	at com.zero.test.java.lang.clazz.Test.main(Test.java:47)
     *
     * @throws NoSuchFieldException
     */
    private static void test12() throws NoSuchFieldException {
        System.out.println("\n====getFields====");
        Field[] fields = 实例类.class.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        // 返回当前类中指定名称的public字段，包括父类/父接口中的public字段
        System.out.println("\n====getField====");
        Field field1 = 实例类.class.getField("实例类字段_public");
        System.out.println(field1.getName());
        Field field2 = 实例类.class.getField("抽象类字段_public");
        System.out.println(field2.getName());
        Field field3 = 实例类.class.getField("接口字段_public");
        System.out.println(field3.getName());
        Field field4 = 实例类.class.getField("接口字段_无");
        System.out.println(field4.getName());
    }

    /**
     * java.util.AbstractMap<K, V>    sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
     *
     * java.util.Map<K, V>    sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl
     * java.lang.Cloneable    java.lang.Class
     * java.io.Serializable    java.lang.Class
     *
     * K    sun.reflect.generics.reflectiveObjects.TypeVariableImpl
     * V    sun.reflect.generics.reflectiveObjects.TypeVariableImpl
     */
    private static void test11() {
        // 获取HashMap类的父类
        Type type = HashMap.class.getGenericSuperclass();
        System.out.println(type.getTypeName()+"    "+type.getClass().getName());

        System.out.println();

        // 获取HashMap类的父接口
        Type[] types = HashMap.class.getGenericInterfaces();
        for(Type t : types){
            System.out.println(t.getTypeName()+"    "+t.getClass().getName());
        }

        System.out.println();

        // 获取HashMap类的两个TypeVariable
        TypeVariable[] tvs = HashMap.class.getTypeParameters();
        for (TypeVariable tv : tvs){
            System.out.println(tv.getTypeName()+"    "+tv.getClass().getName());
        }
    }

    /**
     * Class.getClasses() 获取public的内部类和父类的内部类或接口
     * 实例类_内部接口_public
     * 实例类_内部抽象类_public
     * 实例类_内部实例类_public
     * 抽象类_内部接口_public
     * 抽象类_内部抽象类_public
     * 抽象类_内部实例类_public
     *
     * Class.getDeclaredClasses()获取全部内部类和接口，只获取本类，不获取父类
     * 实例类_内部接口_public
     * 实例类_内部接口_protected
     * 实例类_内部接口_无访问修饰符
     * 实例类_内部接口_private
     * 实例类_内部抽象类_public
     * 实例类_内部抽象类_protected
     * 实例类_内部抽象类_无访问修饰符
     * 实例类_内部抽象类_private
     * 实例类_内部实例类_public
     * 实例类_内部实例类_protected
     * 实例类_内部实例类_无访问修饰符
     * 实例类_内部实例类_private
     */
    private static void test10() {
        // 测试getClasses()
        Class[] classes2 = 实例类.class.getClasses();
        for(Class c : classes2){
            System.out.println(c.getSimpleName());
        }
        System.out.println();

        // 测试getDeclaredClasses()
        Class[] classes1 = 实例类.class.getDeclaredClasses();
        for(Class c : classes1){
            System.out.println(c.getSimpleName());
        }
    }

    /**
     * class com.zero.test.java.lang.clazz.Test
     * class com.zero.test.java.lang.clazz.Test
     * null
     * <p>
     * class com.zero.test.java.lang.clazz.Test
     * null
     * null
     * null
     */
    private static void test09() {
        class 方法内部类 {
        }

        接口09 匿名内部类对象 = new 接口09() {
        };

        // 测试getEnclosingClass()
        System.out.println(成员内部类.class.getEnclosingClass());
        System.out.println(方法内部类.class.getEnclosingClass());
        System.out.println(匿名内部类对象.getClass().getEnclosingClass()); // 匿名内部类
        System.out.println(接口09.class.getEnclosingClass());   // 外部类

        System.out.println();

        // 测试getDeclaringClass()
        System.out.println(成员内部类.class.getDeclaringClass());
        System.out.println(方法内部类.class.getDeclaringClass());
        System.out.println(匿名内部类对象.getClass().getDeclaringClass()); // 匿名内部类
        System.out.println(接口09.class.getDeclaringClass());   // 外部类
    }

    class 成员内部类 {
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
        for (Class c : cs) {
            System.out.println(c);
        }
    }


    /**
     * null
     * int
     * class [I
     * <p>
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
     * <p>
     * clazz.getName() = [[I
     * clazz.getSimpleName() = int[][]
     * clazz.getCanonicalName() = int[][]
     * clazz.getTypeName() = int[][]
     * <p>
     * clazz.getName() = [Ljava.lang.Integer;
     * clazz.getSimpleName() = Integer[]
     * clazz.getCanonicalName() = java.lang.Integer[]
     * clazz.getTypeName() = java.lang.Integer[]
     * <p>
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
     * <p>
     * =================内部类=================
     * clazz.getName() = com.zero.test.java.lang.clazz.Test$innerClass
     * clazz.getSimpleName() = innerClass
     * clazz.getCanonicalName() = com.zero.test.java.lang.clazz.Test.innerClass
     * clazz.getTypeName() = com.zero.test.java.lang.clazz.Test$innerClass
     * <p>
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
        接口05 zObject = new 接口05() {
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
        Class<?> aClass = Class.forName("com.zero.test.java.lang.clazz.模板01.A", false, ClassLoader.getSystemClassLoader());
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
        Class<?> aClass = Class.forName("com.zero.test.java.lang.clazz.模板01.A");
        System.out.println(" ==================== ");
        Object o = aClass.newInstance();
        Object o1 = aClass.newInstance();
        Class<?> bClass = Class.forName("com.zero.test.java.lang.clazz.模板01.A");
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

interface 接口05 {
    void run();
}

interface 接口09 {
}
