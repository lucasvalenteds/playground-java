package com.playground.java.hibernate.onetoone.unidirectional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Person {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_email")
    public Email email;

    public Person() {
    }

    public Person(Email email) {
        this.email = email;
    }
}
