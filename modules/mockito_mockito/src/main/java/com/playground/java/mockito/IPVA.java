package com.playground.java.mockito;

import java.time.Month;

class IPVA implements Tax {

    private final Month monthItWasPaid;

    IPVA(Month monthItWasPaid) {
        this.monthItWasPaid = monthItWasPaid;
    }

    @Override
    public double applyTaxOnValue(double value) {
        if (monthItWasPaid == Month.FEBRUARY) {
            return value + 200;
        } else {
            return value + 150;
        }
    }
}
