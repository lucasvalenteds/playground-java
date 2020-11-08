package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntBinaryOperator;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("jdk-8")
class LambdasTest {

    @DisplayName("The Function interface defines a lambda expression")
    @Test
    void testFunctionType() {
        Function<Integer, Integer> powNumber = number -> number * number;

        assertThat(powNumber).isInstanceOf(Function.class);
        assertThat(powNumber.apply(5)).isEqualTo(25);
    }

    @DisplayName("The Consumer is meant to be used as a void callback")
    @Test
    void testConsumerType() {
        Consumer<String> printMessage = System.out::println;

        assertThat(printMessage).isInstanceOf(Consumer.class);
    }

    @DisplayName("Lambdas can be chained. The compose method is fired before the lambda itself")
    @Test
    void testLambdasCanBeComposed() {
        Function<Integer, Integer> fnMultiplyByTwo = number -> number * 2;

        Function<Integer, Integer> composed = fnMultiplyByTwo  // execution order: second
                .compose((Integer number) -> number + 1)       // execution order: first
                .andThen((Integer number) -> number + 3);      // execution order: third

        assertThat(composed.apply(1)).isEqualTo(7);
    }

    @DisplayName("Lambdas can receive two arguments")
    @Test
    void testLambdasCanReceiveUpToTwoParameters() {
        IntBinaryOperator adder = (int a, int b) -> a + b;

        assertThat(adder.applyAsInt(2,3)).isEqualTo(5);
    }
}
