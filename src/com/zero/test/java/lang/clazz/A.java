package com.zero.test.java.lang.clazz;

public class A {

    static {
        System.out.println("-->静态初始化（只执行一次）<--");
    }

    public A() {
        System.out.println("-->无参数构造方法<--");
    }

    public A(String name) {
        this.name = name;
    }

    public A(int age) {
        this.age = age;
    }

    private String name;

    private int age;

    public static final Integer NUM = 15;

    public void sayHello(String str){
        say(str);
    }
    private void say(String st){
        System.out.println(st);
    }
    public A(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
