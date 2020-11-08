package com.playground.java.failsafe;

import net.jodah.failsafe.Failsafe;
import net.jodah.failsafe.Fallback;
import net.jodah.failsafe.RetryPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Fail.fail;

class MainTest {

    @DisplayName("It can try again when an error happens")
    @Test
    void testSingleException() {
        var policy = new RetryPolicy<String>()
                .handle(RuntimeException.class)
                .withMaxRetries(2);

        assertThatThrownBy(() -> Failsafe.with(policy)
                .onSuccess(message -> fail("This test should fail"))
                .onFailure(error -> {
                    assertThat(error.getResult()).isNull();
                    assertThat(error.getFailure()).isNotNull();
                })
                .run(this::lambdaThatThrowsRuntimeException))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("This method always throws");
    }

    @DisplayName("It can handle multiple types of exceptions")
    @Test
    void testMultipleExceptions() {
        var executor = Failsafe.with(
                new RetryPolicy<>().handle(IllegalArgumentException.class),
                new RetryPolicy<>().handle(RuntimeException.class)
        );

        assertThatThrownBy(() -> executor.run(this::lambdaThatThrowsRuntimeException))
                .isInstanceOf(RuntimeException.class);

        assertThatThrownBy(() -> executor.run(this::lambdaThatThrowsArgumentException))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("It can return a fallback value in case of failure")
    @Test
    void testFallbackValue() {
        var retry = new RetryPolicy<String>();
        var fallback = Fallback.of(this::lambdaThatReturnsValue);

        var message = Failsafe.with(fallback, retry)
                .get(this::lambdaThatReturnsValue);

        assertThat(message).isEqualTo("Hello World!");
    }

    private String lambdaThatReturnsValue() {
        return "Hello World!";
    }

    private void lambdaThatThrowsRuntimeException() throws RuntimeException {
        throw new RuntimeException("This method always throws");
    }

    private void lambdaThatThrowsArgumentException() throws IllegalArgumentException {
        throw new IllegalArgumentException("Some input error");
    }

}
