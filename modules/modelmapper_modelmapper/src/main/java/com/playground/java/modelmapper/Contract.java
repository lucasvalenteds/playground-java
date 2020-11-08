package com.playground.java.modelmapper;

public class Contract {
    private long id;
    private double price;
    private boolean isExpired;

    public Contract() {
        this.id = 0L;
        this.price = 0.0D;
        this.isExpired = false;
    }

    public Contract(long id, double price, boolean isExpired) {
        this.id = id;
        this.price = price;
        this.isExpired = isExpired;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}

