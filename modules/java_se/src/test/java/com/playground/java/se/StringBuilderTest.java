package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("jdk-5")
class StringBuilderTest {

    @DisplayName("It is used to avoid long String concatenations")
    @Test
    void testAvoidConcat() {
        StringBuilder messageBuilder = new StringBuilder()
                .append('#')
                .append(1)
                .append(": ")
                .append("Hello")
                .append(", ")
                .append("Odin")
                .append('!');

        assertThat(messageBuilder.toString()).isEqualTo("#1: Hello, Odin!");
    }

    @DisplayName("It can be used to reverse a String")
    @Test
    void testReverseString() {
        String text = "A car, a man, a maraca";
        String textReversed = new StringBuilder(text).reverse().toString();

        assertThat(textReversed).isEqualTo("acaram a ,nam a ,rac A");
    }
}
