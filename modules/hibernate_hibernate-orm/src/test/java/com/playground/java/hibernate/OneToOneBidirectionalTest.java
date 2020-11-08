package com.playground.java.hibernate;

import com.playground.java.hibernate.onetoone.bidirectional.Address;
import com.playground.java.hibernate.onetoone.bidirectional.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OneToOneBidirectionalTest {

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void beforeEach() {
        entityManagerFactory = Persistence.createEntityManagerFactory("example");
    }

    @AfterEach
    public void afterEach() {
        entityManagerFactory.close();
    }

    private EntityManagerFactory getFactory() {
        return this.entityManagerFactory;
    }

    @DisplayName("One-to-one bidirectional CRUD")
    @Test
    void testBasicCrud() {
        doInJPA(this::getFactory, (entityManager) -> {
            var email = new Address("Main Avenue");
            var person = new Customer(email);

            entityManager.persist(person);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Customer customer = entityManager.find(Customer.class, 1L);
            customer.address.address = "Not Main Avenue";

            entityManager.merge(customer);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Customer customer = entityManager.find(Customer.class, 1L);

            assertEquals("Not Main Avenue", customer.address.address);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Address> query = builder.createQuery(Address.class);
            Root<Address> root = query.from(Address.class);

            Address address = entityManager.createQuery(
                query.where(builder.equal(root.get("address"), "Not Main Avenue"))
            ).getSingleResult();

            assertNotNull(address.customer);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            entityManager.remove(entityManager.find(Customer.class, 1L));
        });
    }
}
