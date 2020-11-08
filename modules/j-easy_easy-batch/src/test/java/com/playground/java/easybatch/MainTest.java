package com.playground.java.easybatch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.writer.CollectionRecordWriter;
import org.easybatch.flatfile.DelimitedRecordMapper;
import org.easybatch.flatfile.FlatFileRecordReader;
import org.junit.jupiter.api.AfterEach;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MainTest {

    private final JobExecutor executor = new JobExecutor();

    @AfterEach
    void tearDown() {
        executor.shutdown();
    }

    @DisplayName("It can map content of a CSV file and map it to POJO")
    @Test
    void testFlatFileToCollection() {
        var customers = new ArrayList<Customer>();

        executor.execute(new JobBuilder()
                .reader(new FlatFileRecordReader(new File("src/test/resources/customers.dat")))
                .mapper(new DelimitedRecordMapper<>(Customer.class, "id", "name"))
                .writer(new CollectionRecordWriter(customers))
                .build());

        assertEquals(3, customers.size());
        assertTrue(customers.containsAll(List.of(
                new Customer(1, "John"),
                new Customer(2, "Jane"),
                new Customer(3, "Jack")
        )));
    }
}
