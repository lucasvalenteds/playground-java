package com.playground.java.guice.assisted;

import com.google.inject.assistedinject.Assisted;

public interface PetFactory {

    public Pet createPet(@Assisted("age") int age);
}
