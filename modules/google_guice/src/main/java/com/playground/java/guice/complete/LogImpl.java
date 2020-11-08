package com.playground.java.guice.complete;

public class LogImpl implements Log {

    @Override
    public String logSystemStart() {
        return "System started 1 second ago";
    }
}
