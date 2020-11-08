package com.playground.java.modelmapper;

public class ResponseMinimal {

    private String personSalary;

    public ResponseMinimal() {
    }

    public ResponseMinimal(String personSalary) {
        this.personSalary = personSalary;
    }

    public String getPersonSalary() {
        return personSalary;
    }

    public void setPersonSalary(String personSalary) {
        this.personSalary = personSalary;
    }
}
