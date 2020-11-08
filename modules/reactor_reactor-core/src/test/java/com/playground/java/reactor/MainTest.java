package com.playground.java.reactor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class MainTest {

    @DisplayName("Generating N random numbers but taking only 5")
    @Test
    void testFluxGenerateTakeLast() {
        int amountOfNumbersToGenerate = 5;

        var numbers = Flux.<Double>generate(sink -> sink.next(Math.random()))
            .take(amountOfNumbersToGenerate);

        StepVerifier.create(numbers)
            .expectNextCount(amountOfNumbersToGenerate)
            .verifyComplete();
    }
}
