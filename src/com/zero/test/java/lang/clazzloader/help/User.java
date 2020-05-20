package com.zero.test.java.lang.clazzloader.help;

public class User {

    public User() {

        ClassLoader classLoader = this.getClass().getClassLoader();
        System.out.println("我是被 " + classLoader.getClass().getName() + " 加载的");
    }
}
