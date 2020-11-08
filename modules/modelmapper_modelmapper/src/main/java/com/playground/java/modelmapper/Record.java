package com.playground.java.modelmapper;

import java.util.List;

class Record {
    private Person person;
    private List<Contract> contractsPrice;

    public Record() {
        this.person = new Person();
        this.contractsPrice = List.of();
    }

    public Record(Person person, List<Contract> contractsPrice) {
        this.person = person;
        this.contractsPrice = contractsPrice;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public List<Contract> getContractsPrice() {
        return contractsPrice;
    }

    public void setContractsPrice(List<Contract> contractsPrice) {
        this.contractsPrice = contractsPrice;
    }
}
