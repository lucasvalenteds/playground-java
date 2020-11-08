package com.playground.java.restassured;

import java.util.Objects;

public class Person {

    private String name;
    private int age;
    private boolean livingInCanada;

    Person(String name, int age, boolean livingInCanada) {
        this.name = name;
        this.age = age;
        this.livingInCanada = livingInCanada;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public boolean isLivingInCanada() {
        return livingInCanada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age &&
                livingInCanada == person.livingInCanada &&
                Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, livingInCanada);
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", livingInCanada=" + livingInCanada +
                '}';
    }
}
