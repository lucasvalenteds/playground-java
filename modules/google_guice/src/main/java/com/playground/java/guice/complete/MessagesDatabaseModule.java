package com.playground.java.guice.complete;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import java.util.List;

public class MessagesDatabaseModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(String.class).toInstance("Hello World!");
        bind(List.class).toProvider(this::provideMessages);
        bind(Database.class).to(DatabaseImpl.class);
    }

    @Provides
    List<Message> provideMessages() {
        return List.of(
                new Message("Message 1"),
                new Message("Message 2"),
                new Message("Message 3"),
                new Message("Message 4"));
    }
}
