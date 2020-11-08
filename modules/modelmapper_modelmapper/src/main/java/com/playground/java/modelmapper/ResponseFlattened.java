package com.playground.java.modelmapper;

public class ResponseFlattened {

    private String personName;
    private int personAge;

    public ResponseFlattened() {
        this.personName = "";
        this.personAge = 0;
    }

    public ResponseFlattened(String personName, int personAge) {
        this.personName = personName;
        this.personAge = personAge;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public int getPersonAge() {
        return personAge;
    }

    public void setPersonAge(int personAge) {
        this.personAge = personAge;
    }
}
