package com.playground.java.se.customer;

public class Customer {

    private final Long id;
    private final String name;
    private final Integer age;

    public Customer(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }
}
