package com.playground.java.se;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import com.playground.java.se.person.LivesInCanada;
import com.playground.java.se.person.Person;
import com.playground.java.se.person.ResearchPair;
import com.playground.java.se.person.Score;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("jdk-8")
class StreamsTest {

    @Nested
    class UsageTest {

        private final List<Person> researchParticipants = List.of(
                new Person("John", 28, LivesInCanada.NO),
                new Person("Mary", 30, LivesInCanada.NO),
                new Person("Dave", 42, LivesInCanada.YES),
                new Person("Jane", 37, LivesInCanada.NO),
                new Person("Igor", 15, LivesInCanada.YES),
                new Person("Anne", 19, LivesInCanada.YES));

        private final List<Score> researchScores = List.of(
                new Score(10.0),
                new Score(9.5),
                new Score(4.9),
                new Score(2.1),
                new Score(6.3),
                new Score(7.8));

        @DisplayName("IntStream, LongStream and DoubleStream are basically lists")
        @Test
        void testStreamConstructors() {
            assertThat(IntStream.of(1, 2, 3)).isEqualTo(List.of(1, 2, 3));
            assertThat(LongStream.of(1L, 2L, 3L)).isEqualTo(List.of(1L, 2L, 3L));
            assertThat(DoubleStream.of(1.1, 2.2, 3.3)).isEqualTo(List.of(1.1, 2.2, 3.3));
        }

        @DisplayName("Filter methods expects a condition as lambda expression")
        @Test
        void testItCanFilter() {
            Stream<Person> olderThan20ThatLivesInCanada = researchParticipants.stream()
                    .filter(it -> it.getAge() > 20)
                    .filter(it -> it.livesInCanada().asBoolean());

            assertThat(olderThan20ThatLivesInCanada)
                    .contains(researchParticipants.get(2))
                    .hasSize(1);
        }

        @DisplayName("map() and mapToInt() expects a lambda expression")
        @Test
        void testItCanMap() {
            Stream<String> namesOnly = researchParticipants.stream()
                    .map(Person::getName);

            IntStream ageOnly = researchParticipants.stream()
                    .mapToInt(Person::getAge);

            assertThat(namesOnly).contains("John", "Mary", "Dave", "Jane", "Igor", "Anne");
            assertThat(ageOnly).contains(28, 30, 42, 37, 15, 19);
        }

        @DisplayName("mapToObject() can be used in a range stream")
        @Test
        void testItCanMapToObject() {
            IntStream rangeOfNumbers = IntStream.range(0, researchParticipants.size());

            List<ResearchPair> pairsOfParticipantAndScore = rangeOfNumbers
                    .mapToObj(index -> new ResearchPair(
                            researchParticipants.get(index),
                            researchScores.get(index)))
                    .collect(Collectors.toList());

            assertThat(pairsOfParticipantAndScore).hasSize(researchParticipants.size());
        }

        @DisplayName("flatMap() exists but without index")
        @Test
        void testItCanFlatMap() {
            List<List<Integer>> nestedNumbers = List.of(List.of(1), List.of(2, 3), List.of(4, 5));

            List<Optional<Integer>> optionalNumbers = nestedNumbers.stream()
                    .flatMap(list -> list.stream().map(Optional::of))
                    .collect(Collectors.toList());

            List<Integer> onlyNumbers = optionalNumbers.stream()
                    .map(Optional::get)
                    .collect(Collectors.toList());

            assertThat(onlyNumbers).contains(1, 2, 3, 4, 5);
        }

        @DisplayName("findFirst() returns an Optional")
        @Test
        void testItCanFindFirst() {
            Optional<Person> firstParticipant = researchParticipants.stream().findFirst();

            assertThat(firstParticipant.isPresent()).isTrue();
            assertThat(firstParticipant.get()).isEqualTo(researchParticipants.get(0));
        }

        @DisplayName("reduce() expects the initial value and the consumer")
        @Test
        void testItCanReduce() {
            int six = IntStream.of(2, 2, 2).reduce(0, Integer::sum);

            assertThat(six).isEqualTo(6);
        }

        @DisplayName("A stream throws IllegalStateException if processed twice")
        @Test
        void testStreamMultipleIterations() {
            Stream<Integer> threeNumbersStream = List.of(1, 2, 3).stream();

            threeNumbersStream.filter(it -> it % 2 == 0);

            assertThatThrownBy(() -> threeNumbersStream.filter(it -> false))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("stream has already been operated upon or closed");
        }

        @DisplayName("An infinite Stream can be stopped by the limit method")
        @Test
        void testInfiniteStream() {
            Stream<Integer> infiniteNumberGenerator = Stream.generate(() -> 0);

            List<Integer> numbersGenerated = infiniteNumberGenerator
                    .limit(5)
                    .collect(Collectors.toList());

            assertThat(numbersGenerated).hasSize(5).contains(0, 0, 0, 0, 0);
        }
    }

    @Nested
    class ParallelTest {

        @DisplayName("Useful for large data")
        @RepeatedTest(10)
        @DisabledIfEnvironmentVariable(named = "CI_SERVER", matches = "yes")
        void testParallelStream() {
            int numbersToProcess = 100_000_000;

            long parallelStart = System.currentTimeMillis();
            int parallelSum = IntStream.range(0, numbersToProcess).parallel().reduce(0, (sum, n) -> sum + n * sum);
            long parallelStop = System.currentTimeMillis();

            long sequentialStart = System.currentTimeMillis();
            int sequentialSum = IntStream.range(0, numbersToProcess).reduce(0, (sum, n) -> sum + n * sum);
            long sequentialStop = System.currentTimeMillis();

            long timeInParallel = parallelStop - parallelStart;
            long timeInSequential = sequentialStop - sequentialStart;

            assertThat(timeInSequential).isNotEqualTo(timeInParallel);
            assertThat(sequentialSum).isEqualTo(parallelSum);
        }
    }

}
