package com.playground.java.spring;

import org.springframework.data.jpa.domain.Specification;

public final class CustomerSpecification {

    private CustomerSpecification() {
    }

    public static Specification<Customer> nameEqualTo(String name) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("name"), name);
    }

    public static Specification<Customer> ageBetween(int min, int max) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.and(
                criteriaBuilder.greaterThan(root.get("age"), min),
                criteriaBuilder.lessThan(root.get("age"), max)
            );
    }

    public static Specification<Customer> livingIn(String cityOrStateOrCountry) {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.like(root.get("address"), "%" + cityOrStateOrCountry + "%");
    }

    public static Specification<Customer> adultsLivingInTheUnitedStates() {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.and(
                ageBetween(21, 100).toPredicate(root, criteriaQuery, criteriaBuilder),
                livingIn("USA").toPredicate(root, criteriaQuery, criteriaBuilder)
            );
    }

    public static Specification<Customer> childrenLivingInLatinAmerica() {
        return (root, criteriaQuery, criteriaBuilder) ->
            criteriaBuilder.and(
                ageBetween(0, 12).toPredicate(root, criteriaQuery, criteriaBuilder),
                criteriaBuilder.or(
                    livingIn("Argentina").toPredicate(root, criteriaQuery, criteriaBuilder),
                    livingIn("Chile").toPredicate(root, criteriaQuery, criteriaBuilder),
                    livingIn("Mexico").toPredicate(root, criteriaQuery, criteriaBuilder)
                )
            );
    }
}
