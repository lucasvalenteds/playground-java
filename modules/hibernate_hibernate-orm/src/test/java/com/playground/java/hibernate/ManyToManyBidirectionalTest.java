package com.playground.java.hibernate;

import com.playground.java.hibernate.manytomany.bidirectional.Project;
import com.playground.java.hibernate.manytomany.bidirectional.Team;
import org.assertj.core.api.Assertions;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManyToManyBidirectionalTest {

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

    @DisplayName("Many-to-many bidirectional CRUD")
    @Test
    void testBasicCrud() {
        doInJPA(this::getFactory, (entityManager) -> {
            Project project = new Project();
            project.name = "Software XYZ";
            entityManager.persist(project);

            List<Team> teams = new ArrayList<>(2);
            teams.add(new Team("DEV"));
            teams.add(new Team("UX"));
            teams.forEach(entityManager::persist);

            project.teams = teams;
            entityManager.merge(project);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Project project = entityManager.find(Project.class, 1L);

            assertEquals(1L, project.id);
            assertEquals("Software XYZ", project.name);
            assertEquals(2, project.teams.size());
            Assertions.assertThat(project.teams).extracting(it -> it.code).containsOnly("DEV", "UX");
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Project project = entityManager.find(Project.class, 1L);
            Team team = new Team("OPS");
            entityManager.persist(team);
            project.teams.add(team);
            entityManager.merge(project);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Team> query = builder.createQuery(Team.class);
            Root<Team> root = query.from(Team.class);

            List<Team> teams = entityManager.createQuery(query.select(root)).getResultList();

            assertEquals(3, teams.size());
            assertThat(teams).extracting(it -> it.code).containsOnly("DEV", "UX", "OPS");
        });
        doInJPA(this::getFactory, (entityManager) -> {
            entityManager.remove(entityManager.find(Project.class, 1L));

            assertNull(entityManager.find(Project.class, 1L));
            assertNull(entityManager.find(Team.class, 2L));
            assertNull(entityManager.find(Team.class, 3L));
            assertNull(entityManager.find(Team.class, 4L));
        });
    }
}
