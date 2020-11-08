package com.playground.java.hibernate.minimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Item {

    @Id
    public Long id;

    public String name;
}
