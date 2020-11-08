package com.playground.java.hibernate;

import com.playground.java.hibernate.customconfiguration.Word;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumSet;
import java.util.Map;

import static org.hibernate.testing.transaction.TransactionUtil.doInJPA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomConfigurationTest {

    private static final Path RESOURCES = Paths.get("src", "test", "resources");
    private static final String SQL_FILENAME = "schema.sql";
    private static final String SQL_SCHEMA = """
        drop table Word if exists
        drop sequence if exists hibernate_sequence
        create sequence hibernate_sequence start with 1 increment by 1
        create table Word (id bigint not null, word varchar(255), primary key (id))
        """;

    @DisplayName("Exporting SQL DDL to a file")
    @Test
    void testCreatingConfigurationProgrammatically() throws IOException {
        assertTrue(Files.notExists(RESOURCES.resolve(SQL_FILENAME)));

        Metadata metadata = createMetadata();

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setOutputFile(RESOURCES.resolve(SQL_FILENAME).toString());
        schemaExport.execute(
            EnumSet.of(TargetType.SCRIPT, TargetType.STDOUT),
            SchemaExport.Action.BOTH,
            metadata
        );

        assertEquals(SQL_SCHEMA, Files.readString(RESOURCES.resolve(SQL_FILENAME)));

        assertTrue(Files.deleteIfExists(RESOURCES.resolve(SQL_FILENAME)));
    }

    @DisplayName("Running queries")
    @Test
    void testRunningOneQueryUsingProgrammaticConfiguration() {
        doInJPA(createMetadata()::buildSessionFactory, (entityManager) -> {
            Word word = new Word();
            word.word = "Banana";
            entityManager.persist(word);

            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Word> query = builder.createQuery(Word.class);
            Root<Word> root = query.from(Word.class);
            CriteriaQuery<Word> selectLike = query.select(root).where(builder.like(root.get("word"), "%na%"));
            Word banana = entityManager.createQuery(selectLike).getSingleResult();

            assertNotNull(banana);
            assertEquals(1L, banana.id);
            assertEquals("Banana", banana.word);
        });
    }

    private Metadata createMetadata() {
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
            .applySettings(Map.ofEntries(
                Map.entry("hibernate.dialect", "org.hibernate.dialect.H2Dialect"),
                Map.entry("hibernate.connection.driver_class", "org.h2.Driver"),
                Map.entry("hibernate.connection.url", "jdbc:h2:mem:db1;DB_CLOSE_DELAY=-1;MVCC=true"),
                Map.entry("hibernate.connection.username", "sa"),
                Map.entry("hibernate.connection.pool_size", 5),
                Map.entry("hibernate.show_sql", "true"),
                Map.entry("hibernate.format_sql", "true"),
                Map.entry("hibernate.hbm2ddl.auto", "create-drop")
            ))
            .build();

        MetadataSources metadataSources = new MetadataSources(serviceRegistry);
        metadataSources.addAnnotatedClass(Word.class);

        return metadataSources.buildMetadata();
    }
}
