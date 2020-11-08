package com.playground.java.guice.complete;

import com.google.inject.Inject;

import java.util.List;
import java.util.Optional;

public class DatabaseImpl implements Database {

    private final List<Message> messages;

    @Inject
    public DatabaseImpl(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public Optional<List<Message>> getMessages() {
        return Optional.of(messages);
    }
}
