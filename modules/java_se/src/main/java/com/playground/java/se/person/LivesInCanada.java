package com.playground.java.se.person;

public enum LivesInCanada {
    YES(true), NO(false);

    private final Boolean bool;

    LivesInCanada(boolean bool) {
        this.bool = bool;
    }

    public Boolean asBoolean() {
        return bool;
    }
}
