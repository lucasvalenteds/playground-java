package com.playground.java.hibernate;

import com.playground.java.hibernate.onetoone.unidirectional.Email;
import com.playground.java.hibernate.onetoone.unidirectional.Person;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OneToOneUnidirectionalTest {

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

    @DisplayName("One-to-one unidirectional CRUD")
    @Test
    void testBasicCrud() {
        doInJPA(this::getFactory, (entityManager) -> {
            var email = new Email("john.smith@email.com");
            var person = new Person(email);

            entityManager.persist(person);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Person person = entityManager.find(Person.class, 1L);
            person.email.address = "john@email.com";

            entityManager.merge(person);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Person person = entityManager.find(Person.class, 1L);

            assertEquals("john@email.com", person.email.address);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            entityManager.remove(entityManager.find(Person.class, 1L));
        });
    }
}
