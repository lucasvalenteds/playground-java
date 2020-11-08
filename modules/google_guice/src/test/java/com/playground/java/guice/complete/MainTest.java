package com.playground.java.guice.complete;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.ProvisionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

class MainTest {

    @DisplayName("A module is intended to associate types with implementations")
    @Test
    void testModuleProvidesTheInstances() {
        Injector injector = Guice.createInjector(binder -> {
            binder.bind(Message.class).toProvider(() -> new Message("Hi, Dave!"));
            binder.bind(Log.class).to(LogImpl.class);
        });

        try {
            Log logger = injector.getInstance(Log.class);
            assertThat(logger).isNotNull();
            assertThat(logger.logSystemStart()).isEqualTo("System started 1 second ago");

            Message message = injector.getInstance(Message.class);
            assertThat(message).isNotNull();
            assertThat(message.getText()).isEqualTo("Hi, Dave!");
        } catch (ProvisionException exception) {
            fail("The module does not have an expected bind.", exception);
        }
    }

    @DisplayName("A module can associate values by type, functions and concrete instances")
    @Test
    void testModulesCanProvideComplexTypes() {
        Injector injector = Guice.createInjector(new MessagesDatabaseModule());

        String helloWorldText = injector.getInstance(String.class);
        List messages = injector.getInstance(List.class);
        Database database = injector.getInstance(Database.class);

        assertThat(helloWorldText)
                .isNotNull()
                .isEqualTo("Hello World!");

        assertThat(messages)
                .isNotNull()
                .hasSize(4);

        assertThat(database).isNotNull();
        database.getMessages().ifPresentOrElse(
                allMessages -> assertThat(allMessages).hasSize(4),
                () -> fail("Messages should be able to be retrieved."));
    }

}
