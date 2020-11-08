package com.playground.java.spring;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @Column
    public Integer age;

    @Column
    public String address;

    public Customer() {
    }

    public Customer(String name, Integer age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }
}
