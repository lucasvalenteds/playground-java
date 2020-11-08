package com.playground.java.hibernate.manytomany.bidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String code;

    @ManyToMany(mappedBy = "teams")
    public List<Project> projects;

    public Team() {
    }

    public Team(String code) {
        this.code = code;
    }
}
