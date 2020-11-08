package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;

@Tag("jdk-8")
class OptionalTest {

    private final Optional<String> of = Optional.of("Hello World");
    private final Optional<String> empty = Optional.empty();
    private final Optional<String> nullable = Optional.ofNullable(null);

    @DisplayName("It can be create using of(), ofNullable() and empty()")
    @Test
    void testHowToInstanceOneOptional() {
        assertThat(of).isInstanceOf(Optional.class);
        assertThat(empty).isInstanceOf(Optional.class);
        assertThat(nullable).isInstanceOf(Optional.class);
    }

    @DisplayName("Using get() in a nullable or empty Optional throws NoSuchElement")
    @Test
    void testGetOnNullableThrowsException() {
        assertThatThrownBy(nullable::get).isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("get() and orElse() can be used instead of null check or isPresent()")
    @Test
    void testGetAndOrElse() {
        assertThat(of.get()).isEqualTo("Hello World");
        assertThat(of.orElse("")).isNotEmpty().isEqualTo("Hello World");
        assertThat(empty.orElse("Not empty haha")).isEqualTo("Not empty haha");
        assertThat(nullable.orElse("We dont't need no null")).isNotEmpty();
    }

    @DisplayName("ifPresent can be used with a callback")
    @Test
    void testIfPresentCallback() {
        of.ifPresent(text -> assertThat(text).isEqualTo("Hello World"));
        empty.ifPresent(text -> fail("Empty Optional should not have present value"));
        nullable.ifPresent(text -> fail("Nullable Optional should not have present value"));
    }

    @DisplayName("isPresent returns true in case an object is not null")
    @Test
    void testIsPresent() {
        assertThat(of.isPresent()).isTrue();
        assertThat(empty.isPresent()).isFalse();
        assertThat(nullable.isPresent()).isFalse();
    }

}
