package com.playground.java.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = {TestingConfiguration.class},
    loader = AnnotationConfigContextLoader.class
)
class SpecificationTest {

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void beforeEach() {
        assertNotNull(repository);

        repository.saveAll(List.of(
            new Customer("John Smith", 45, "Denver, Colorado, USA"),
            new Customer("Rosa Lopez", 28, "Guadalajara, Jalisco, Mexico"),
            new Customer("Jane Evans", 17, "Austin, Nevada, USA"),
            new Customer("Dave Brown", 12, "Montreal, Quebec, Canada"),
            new Customer("Luis Rojas", 8, "Los Loros, Atacama, Chile")
        ));
    }

    @AfterEach
    public void afterEach() {
        assertNotNull(repository);

        repository.deleteAll();
    }

    @DisplayName("Finding customers by exact name")
    @Test
    void testCustomerName() {
        List<Customer> customers = repository.findAll(CustomerSpecification.nameEqualTo("Dave Brown"));

        assertEquals(1, customers.size());
        assertThat(customers).extracting(it -> it.name).containsOnly("Dave Brown");
    }

    @DisplayName("Finding all customers within an age range")
    @Test
    void testCustomerAgeRange() {
        var minAge = 15;
        var maxAge = 35;

        List<Customer> customers = repository.findAll(CustomerSpecification.ageBetween(minAge, maxAge));

        assertEquals(2, customers.size());
        assertThat(customers).extracting(it -> it.name).containsOnly("Rosa Lopez", "Jane Evans");
    }

    static Stream<Arguments> addresses() {
        return Stream.of(
            Arguments.of("Mexico", List.of("Rosa Lopez")),
            Arguments.of("Canada", List.of("Dave Brown")),
            Arguments.of("USA", List.of("John Smith", "Jane Evans")),
            Arguments.of("Nevada", List.of("Jane Evans")),
            Arguments.of("Colorado", List.of("John Smith"))
        );
    }

    @DisplayName("Finding all customers by address")
    @ParameterizedTest
    @MethodSource("addresses")
    void testCustomerAddress(String address, List<String> expectedCustomers) {
        List<Customer> customers = repository.findAll(CustomerSpecification.livingIn(address));

        assertEquals(expectedCustomers.size(), customers.size());
        assertThat(customers).extracting(it -> it.name).containsOnlyOnce(expectedCustomers.toArray(String[]::new));
    }

    @DisplayName("Finding all adults living in the US")
    @Test
    void testCustomerAgeAndAddress() {
        List<Customer> customers = repository.findAll(CustomerSpecification.adultsLivingInTheUnitedStates());

        assertEquals(1, customers.size());
        assertThat(customers).extracting(it -> it.name).containsOnly("John Smith");
    }

    @DisplayName("Counting all children living in Latin America")
    @Test
    void testCustomerCounting() {
        long amountOfCustomer = repository.count(CustomerSpecification.childrenLivingInLatinAmerica());

        assertEquals(1, amountOfCustomer);
    }
}
