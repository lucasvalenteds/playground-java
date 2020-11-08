package com.playground.java.hibernate.manytoone.bidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class PostComment {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String description;

    @ManyToOne(fetch = FetchType.LAZY)
    public Post post;
}
