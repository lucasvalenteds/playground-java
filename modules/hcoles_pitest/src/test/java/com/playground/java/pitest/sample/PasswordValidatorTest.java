package com.playground.java.pitest.sample;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PasswordValidatorTest {

    private final PasswordValidator password = new PasswordValidator();

    @Test
    void testItCanBeTenCharactersLong() {
        final String password = "longplease";

        assertThat(password.length()).isEqualTo(10);
        assertThat(this.password.validate(password)).isTrue();
    }

    @Test
    void testItCannotBeSixCharactersLong() {
        final String password = "dances";

        assertThat(password.length()).isEqualTo(6);
        assertThat(this.password.validate(password)).isFalse();
    }
}
