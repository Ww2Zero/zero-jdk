package com.zero.test.java.lang.clazz.模板03;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface 注解_不可继承_default {
    String value();
}
