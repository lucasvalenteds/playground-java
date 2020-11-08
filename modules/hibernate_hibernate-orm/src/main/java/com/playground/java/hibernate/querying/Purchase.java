package com.playground.java.hibernate.querying;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Purchase {

    @Id
    @GeneratedValue
    public Long id;

    @ManyToMany
    @JoinTable(
        joinColumns = @JoinColumn(name = "order_id"),
        inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    public List<Product> products = new ArrayList<>(0);

    public Purchase() {
    }

    public Purchase(List<Product> products) {
        this.products = products;
    }
}
