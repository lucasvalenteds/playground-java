package com.playground.java.hibernate.onetomany.unidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Phone {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String number;

    @Column(name = "employee_id")
    public Long employeeId;

    public Phone() {
    }

    public Phone(String number) {
        this.number = number;
    }
}
