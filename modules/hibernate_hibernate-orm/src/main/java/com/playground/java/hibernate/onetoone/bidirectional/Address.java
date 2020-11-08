package com.playground.java.hibernate.onetoone.bidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Address {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String address;

    @OneToOne(mappedBy = "address")
    public Customer customer;

    public Address() {
    }

    public Address(String address) {
        this.address = address;
    }
}
