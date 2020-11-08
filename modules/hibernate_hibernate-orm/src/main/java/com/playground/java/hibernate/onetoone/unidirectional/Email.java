package com.playground.java.hibernate.onetoone.unidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Email {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String address;

    @OneToOne
    public Person person;

    public Email() {
    }

    public Email(String address) {
        this.address = address;
    }
}
