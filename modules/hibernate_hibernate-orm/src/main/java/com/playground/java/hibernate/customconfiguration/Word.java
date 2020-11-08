package com.playground.java.hibernate.customconfiguration;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Word {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String word;
}
