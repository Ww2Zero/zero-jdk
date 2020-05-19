package com.zero.test.java.lang.clazz.模板03;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface 注解_可继承_public {
    String value();
}
