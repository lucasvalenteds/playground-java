package com.playground.java.guice.complete;

import java.util.List;
import java.util.Optional;

public interface Database {

    Optional<List<Message>> getMessages();
}
