package com.playground.java.hibernate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.playground.java.hibernate.minimal.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class MinimalTest {

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

    @DisplayName("Minimal CRUD")
    @Test
    void testBasicCrud() {
        createAnItem();
        readAnItem();
        updateAnItem();
        removeAnItem();
    }

    @DisplayName("Querying using a type-safe DSL")
    @Test
    void testCustomQuery() {
        createAnItem();
        queryAnItem();
        removeAnItem();
    }

    private void createAnItem() {
        doInJPA(this::getFactory, (entityManager) -> {
            Item pencil = new Item();
            pencil.id = 1L;
            pencil.name = "Pencil";

            entityManager.persist(pencil);
        });
    }

    private void readAnItem() {
        doInJPA(this::getFactory, (entityManager) -> {
            Item item = entityManager.find(Item.class, 1L);

            assertEquals(1L, item.id);
            assertEquals("Pencil", item.name);
        });
    }

    private void updateAnItem() {
        doInJPA(this::getFactory, (entityManager) -> {
            Item item = entityManager.find(Item.class, 1L);

            item.name = "Pen";
            entityManager.merge(item);

            assertEquals(1L, item.id);
            assertEquals("Pen", item.name);
        });
    }

    private void removeAnItem() {
        doInJPA(this::getFactory, (entityManager) -> {
            Item item = entityManager.find(Item.class, 1L);

            entityManager.remove(item);

            assertFalse(entityManager.contains(entityManager.find(Item.class, 1L)));
        });
    }

    private void queryAnItem() {
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Item> criteria = builder.createQuery(Item.class);
            Root<Item> table = criteria.from(Item.class);
            criteria.select(table);
            criteria.where(builder.equal(table.get("id"), 1L));

            Item pencil = entityManager.createQuery(criteria).getSingleResult();
            assertEquals(1L, pencil.id);
            assertEquals("Pencil", pencil.name);
        });
    }
}
