package com.playground.java.hibernate.onetoone.bidirectional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Customer {

    @Id
    @GeneratedValue
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_address")
    public Address address;

    public Customer() {
    }

    public Customer(Address address) {
        this.address = address;
    }
}
