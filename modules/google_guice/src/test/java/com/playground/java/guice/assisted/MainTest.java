package com.playground.java.guice.assisted;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MainTest {

    @Test
    void testItCanCreatePetsWithoutNew() {
        Injector injector = Guice.createInjector(new PetModule());
        PetFactory factory = injector.getInstance(PetFactory.class);

        Pet rex = factory.createPet(1);

        assertEquals(1, rex.getAge());
    }

    @Test
    void testInjectAnnotationWorks() {
        Injector injector = Guice.createInjector(new PetModule());
        Application application = injector.getInstance(Application.class);

        assertNotNull(application);

        int age = application.getRexAge();
        assertEquals(1, age);
    }
}
