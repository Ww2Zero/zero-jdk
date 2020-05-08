package com.zero.test.java.lang.object.hashcode;

public class Person03 {
    private String name;
    private Integer age;

    public Person03(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }
}
