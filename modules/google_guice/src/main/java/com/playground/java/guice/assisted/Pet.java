package com.playground.java.guice.assisted;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

public class Pet {

    private final int age;

    @AssistedInject
    public Pet(@Assisted("age") int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }
}
