package com.playground.java.hibernate;

import com.playground.java.hibernate.manytoone.bidirectional.Post;
import com.playground.java.hibernate.manytoone.bidirectional.PostComment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ManyToOneBidirectionalTest {

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

    @DisplayName("Many-to-one bidirectional CRUD")
    @Test
    void testBasicCrud() {
        doInJPA(this::getFactory, (entityManager) -> {
            PostComment comment1 = new PostComment();
            comment1.description = "That's great!";

            PostComment comment2 = new PostComment();
            comment2.description = "Congratulations!";

            Post post = new Post();
            post.title = "BREAKING NEWS!";
            post.addComment(comment1);
            post.addComment(comment2);

            entityManager.persist(post);
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Post post = entityManager.find(Post.class, 1L);

            assertEquals("BREAKING NEWS!", post.title);
            assertEquals(2, post.comments.size());
        });
        doInJPA(this::getFactory, (entityManager) -> {
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<PostComment> commentQuery = builder.createQuery(PostComment.class);
            Root<PostComment> commentRoot = commentQuery.from(PostComment.class);
            CriteriaQuery<PostComment> select = commentQuery.where(builder.equal(commentRoot.get("post"), 1L));

            List<PostComment> comments = entityManager.createQuery(select).getResultList();

            assertEquals(2, comments.size());
            assertThat(comments)
                .extracting(it -> it.description)
                .containsOnly("That's great!", "Congratulations!");
        });
        doInJPA(this::getFactory, (entityManager) -> {
            PostComment comment = entityManager.find(PostComment.class, 2L);
            assertNotNull(comment);

            entityManager.remove(comment);

            assertNull(entityManager.find(PostComment.class, 2L));
        });
        doInJPA(this::getFactory, (entityManager) -> {
            Post post = entityManager.find(Post.class, 1L);
            assertNotNull(post);

            entityManager.remove(post);

            assertNull(entityManager.find(Post.class, 1L));
            assertNull(entityManager.find(PostComment.class, 3L));
        });
    }
}
