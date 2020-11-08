package com.playground.java.hibernate.onetomany.unidirectional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Employee {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    public List<Phone> phones;

    public Employee() {
    }

    public Employee(String name) {
        this.name = name;
    }
}
