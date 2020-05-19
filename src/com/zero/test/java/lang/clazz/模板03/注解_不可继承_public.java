package com.zero.test.java.lang.clazz.模板03;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface 注解_不可继承_public {
    String value();
}
