package com.playground.java.guice.assisted;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class PetModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder().build(PetFactory.class));
        bind(Application.class);
    }
}
