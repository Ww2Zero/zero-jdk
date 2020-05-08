package com.zero.test.java.lang.object.hashcode;

public class Person04 {
    private String name;
    private Integer age;

    public Person04(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (age != null ? age.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person04)) return false;

        Person04 person04 = (Person04) o;

        if (name != null ? !name.equals(person04.name) : person04.name != null) return false;
        return age != null ? age.equals(person04.age) : person04.age == null;
    }
}
