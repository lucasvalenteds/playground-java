package com.playground.java.hibernate;

import io.codearte.jfairy.Fairy;
import com.playground.java.hibernate.querying.Product;
import com.playground.java.hibernate.querying.Purchase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;

class QueryingTest {

    private static final Fairy fairy = Fairy.create();

    private EntityManagerFactory entityManagerFactory;

    @BeforeEach
    public void beforeEach() {
        entityManagerFactory = Persistence.createEntityManagerFactory("example");

        doInJPA(this::getFactory, (entityManager) -> {
            Stream.iterate(0, (number) -> number + 1)
                .limit(20)
                .map((it) -> new Product(
                    fairy.textProducer().word(1),
                    fairy.baseProducer().randomBetween(0.50, 15.0),
                    fairy.baseProducer().randomElement("grocery", "kitchen", "construction")
                ))
                .forEach(entityManager::persist);
        });

        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = builder.createQuery(Product.class);
            List<Product> products = entityManager
                .createQuery(query.select(query.from(Product.class)))
                .getResultList();

            Stream.iterate(0, (number) -> number++)
                .limit(5)
                .map((it) -> fairy.baseProducer().randomInt(15))
                .map((it) -> fairy.baseProducer().randomElements(products, it))
                .map(Purchase::new)
                .forEach(entityManager::persist);
        });
    }

    @AfterEach
    public void afterEach() {
        entityManagerFactory.close();
    }

    private EntityManagerFactory getFactory() {
        return this.entityManagerFactory;
    }

    @DisplayName("Querying data using ORDER BY")
    @Test
    void testQueryingOrderBy() {
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Product> query = builder.createQuery(Product.class);
            Root<Product> root = query.from(Product.class);

            List<Double> prices = entityManager.createQuery(query.orderBy(builder.desc(root.get("price"))))
                .setMaxResults(2)
                .getResultStream()
                .map(it -> it.price)
                .collect(Collectors.toList());

            assertEquals(2, prices.size());
            assertThat(prices).isSortedAccordingTo(Comparator.reverseOrder());
        });
    }

    @DisplayName("Querying data using SUM function")
    @Test
    void testQueryingSum() {
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();

            CriteriaQuery<Product> queryProduct = builder.createQuery(Product.class);
            Double sumSample = entityManager.createQuery(
                queryProduct.select(queryProduct.from(Product.class))
            )
                .getResultStream()
                .limit(5)
                .map((it) -> it.price)
                .reduce(0.0, Double::sum);


            CriteriaQuery<Double> querySum = builder.createQuery(Double.class);
            Double sum = entityManager.createQuery(
                querySum.select(builder.sumAsDouble(querySum.from(Product.class).get("price")))
            )
                .getSingleResult();

            assertThat(sum).isGreaterThan(sumSample);
        });
    }
}
