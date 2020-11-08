package com.playground.java.hibernate.querying;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    public Long id;

    @Column
    public String label;

    @Column
    public Double price;

    @Column
    public String category;

    @ManyToMany(mappedBy = "products")
    public List<Purchase> purchases;

    public Product() {
    }

    public Product(String label, Double price, String category) {
        this.label = label;
        this.price = price;
        this.category = category;
    }
}
