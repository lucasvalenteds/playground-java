package com.playground.java.hibernate;

import com.playground.java.hibernate.onetomany.unidirectional.Employee;
import com.playground.java.hibernate.onetomany.unidirectional.Phone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;

class OneToManyUnidirectionalTest {

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

    @DisplayName("One-to-many unidirectional CRUD without join table")
    @Test
    void testBasicCrud() {
        doInJPA(this::getFactory, (entityManager) -> {
            Phone phoneWork = new Phone("(555) 555-1234");
            Phone phoneHome = new Phone("(555) 555-4321");

            Employee employee = new Employee("John Smith");
            List<Phone> phones = new ArrayList<>(2);
            phones.add(phoneWork);
            phones.add(phoneHome);
            employee.phones = phones;

            entityManager.persist(employee);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Employee employee = entityManager.find(Employee.class, 1L);

            assertEquals("John Smith", employee.name);
            assertEquals(2, employee.phones.size());
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Employee employee = entityManager.find(Employee.class, 1L);
            employee.phones = employee.phones.stream()
                .map(it -> {
                    Phone phone = new Phone();
                    phone.id = it.id;
                    phone.number = "+11 ".concat(it.number);
                    phone.employeeId = it.employeeId;
                    return phone;
                })
                .collect(Collectors.toList());

            entityManager.merge(employee);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Phone> query = builder.createQuery(Phone.class);
            Root<Phone> root = query.from(Phone.class);

            List<Phone> phones = entityManager.createQuery(
                query.where(builder.equal(root.get("employeeId"), 1L))
            ).getResultList();

            assertEquals(2, phones.size());
            assertThat(phones).extracting(it -> it.employeeId).containsOnly(1L);
            assertThat(phones).extracting(it -> it.number).containsOnly("+11 (555) 555-1234", "+11 (555) 555-4321");
        });
        doInJPA(this::getFactory, (entityManager) -> {
            entityManager.remove(entityManager.find(Employee.class, 1L));

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Phone> query = builder.createQuery(Phone.class);
            Root<Phone> root = query.from(Phone.class);

            List<Phone> phones = entityManager.createQuery(
                query.where(builder.equal(root.get("employeeId"), 1L))
            ).getResultList();

            assertEquals(0, phones.size());
        });
    }
}
