package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@Tag("jdk-8")
class FuturesTest {

    @DisplayName("A CompletableFuture encapsulate an asynchronous operation")
    @Test
    void testFutureEncapsulateAnAsyncOperation() {
        CompletableFuture<String> getMessageTask = CompletableFuture.supplyAsync(() -> "Hello World!");

        try {
            assertThat(getMessageTask.isCancelled()).isFalse();
            assertThat(getMessageTask.get()).isEqualTo("Hello World!");
        } catch (InterruptedException | ExecutionException exception) {
            fail("The future should succeed", exception);
        }
    }

    @DisplayName("A async operation can be interrupted")
    @RepeatedTest(10)
    void testInterruption() {
        CompletableFuture<Integer> longCalculation = null;
        try {
            longCalculation = CompletableFuture
                    .supplyAsync(() -> 1_000_000 * 1_000_000 * 1_000_000);

            assertThat(longCalculation.isCancelled()).isFalse();
            longCalculation.cancel(false);
        } catch (CancellationException ex) {
            assertThat(longCalculation.isCancelled()).isTrue();
        }
    }

    @DisplayName("A CompletableFuture provides a blocking method")
    @Test
    void testBlockingMethod() {
        try {
            CompletableFuture<String> message = CompletableFuture.completedFuture("Hello World");

            assertThat(message.get()).isEqualTo("Hello World");
        } catch (ExecutionException | InterruptedException exception) {
            fail("The Future should succeed", exception);
        }
    }
}
