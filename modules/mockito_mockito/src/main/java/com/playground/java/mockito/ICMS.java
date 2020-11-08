package com.playground.java.mockito;

public class ICMS implements Tax {

    @Override
    public double applyTaxOnValue(double value) {
        return value * 2;
    }
}
