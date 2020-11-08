package com.playground.java.guice.assisted;

import javax.inject.Inject;

public class Application {

    @Inject
    private PetFactory factory;

    public int getRexAge() {
        Pet rex = factory.createPet(1);

        return rex.getAge();
    }
}
