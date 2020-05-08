package com.zero.test.java.lang.object.equals;


public class Person02 {
    private String name;
    private Integer age;

    public Person02(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person02)) return false;

        Person02 person02 = (Person02) o;

        if (name != null ? !name.equals(person02.name) : person02.name != null) return false;
        return age != null ? age.equals(person02.age) : person02.age == null;
    }
}
