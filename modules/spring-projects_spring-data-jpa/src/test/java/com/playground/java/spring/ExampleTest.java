package com.playground.java.spring;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
    classes = TestingConfiguration.class,
    loader = AnnotationConfigContextLoader.class
)
class ExampleTest {

    @Autowired
    private CustomerRepository repository;

    @BeforeEach
    public void beforeEach() {
        assertNotNull(repository);

        repository.saveAll(List.of(
            new Customer("John Smith", 45, "Denver, Colorado, USA")
        ));
    }

    @AfterEach
    public void afterEach() {
        assertNotNull(repository);

        repository.deleteAll();
    }

    static Stream<Arguments> addresses() {
        return Stream.of(
            Arguments.of("Denver", 1),
            Arguments.of("New Jersey", 0)
        );
    }

    @DisplayName("Finding customer by exact name and similar address")
    @ParameterizedTest
    @MethodSource("addresses")
    void testCustomerExample(String city, int amountOfCustomers) {
        Customer customer = new Customer("John Smith", 45, city);

        List<Customer> customers = repository.findAll(Example.of(customer, ExampleMatcher.matchingAll()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("address", ExampleMatcher.GenericPropertyMatcher::contains)
            .withIgnorePaths("id", "age")
        ));

        assertEquals(amountOfCustomers, customers.size());
    }
}
