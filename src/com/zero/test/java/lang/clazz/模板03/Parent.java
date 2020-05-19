package com.zero.test.java.lang.clazz.模板03;

@注解_不可继承_public(value = "父类中不可继承的注解")
@注解_不可继承_default(value = "父类中不可继承的注解，且只允许在当前包内使用")
@注解_可继承_public(value = "父类中可继承的注解")
@注解_可继承_default(value = "父类中可继承的注解，且只允许在当前包内使用")
public class Parent {
}
