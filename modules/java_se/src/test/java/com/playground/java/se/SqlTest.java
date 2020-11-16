package com.playground.java.se;

import com.playground.java.se.customer.Customer;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SqlTest {

    private static final String DATABASE_URL = "jdbc:h2:~/playground";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "sa";

    private static final String QUERY_CREATE_CUSTOMER = """
        CREATE TABLE customer(
            id IDENTITY NOT NULL PRIMARY KEY,
            name VARCHAR NOT NULL,
            age INT NOT NULL
        );
        """;

    private static final String QUERY_DROP_CUSTOMER = """
        DROP TABLE IF EXISTS customer;
        """;

    private static final String QUERY_INSERT_CUSTOMER = """
        INSERT INTO customer (name, age) VALUES (?, ?);
        """;

    private static final String QUERY_SELECT_CUSTOMER = """
        SELECT c.id, c.name, c.age FROM customer c;
        """;

    private static final String QUERY_SELECT_CUSTOMER_NAME_LIKE = """
        SELECT c.name, c.age FROM customer c WHERE c.name LIKE CONCAT('%',?,'%');
        """;

    private static final String QUERY_UPDATE_CUSTOMER = """
        UPDATE customer c SET c.name = ? WHERE c.id = ?;
        """;

    private static final String QUERY_DELETE_CUSTOMER = """
        DELETE FROM customer c WHERE c.id = ?;
        """;

    private JdbcConnectionPool connectionPool;
    private Connection connection;

    @BeforeEach
    void beforeEach() throws SQLException {
        connectionPool = JdbcConnectionPool.create(DATABASE_URL, USERNAME, PASSWORD);
        connection = connectionPool.getConnection();

        connection.createStatement().execute(QUERY_DROP_CUSTOMER);
        connection.createStatement().execute(QUERY_CREATE_CUSTOMER);

        PreparedStatement customer1 = connection.prepareStatement(QUERY_INSERT_CUSTOMER);
        customer1.setString(1, "John Smith");
        customer1.setInt(2, 45);
        customer1.execute();

        PreparedStatement customer2 = connection.prepareStatement(QUERY_INSERT_CUSTOMER);
        customer2.setString(1, "Mary Jane");
        customer2.setInt(2, 32);
        customer2.execute();
    }

    @AfterEach
    void afterEach() throws SQLException {
        connection.close();
        connectionPool.dispose();
    }

    @DisplayName("Selecting multiple rows")
    @Test
    void testSelectMultipleRows() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_CUSTOMER);
        ResultSet resultSet = statement.executeQuery();

        List<Customer> customers = new ArrayList<>(2);
        while (resultSet.next()) {
            customers.add(new Customer(
                resultSet.getLong(1),
                resultSet.getString(2),
                resultSet.getInt(3)
            ));
        }

        assertEquals(2, customers.size());
        assertEquals(1L, customers.get(0).getId());
        assertEquals("John Smith", customers.get(0).getName());
        assertEquals(45, customers.get(0).getAge());
        assertEquals(2L, customers.get(1).getId());
        assertEquals("Mary Jane", customers.get(1).getName());
        assertEquals(32, customers.get(1).getAge());
    }

    @DisplayName("Selecting single row")
    @Test
    void testSelectSingleRow() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_SELECT_CUSTOMER_NAME_LIKE);
        statement.setString(1, "Smith");

        ResultSet resultSet = statement.executeQuery();
        resultSet.first();
        String name = resultSet.getString("name");
        Integer age = resultSet.getInt("age");

        assertEquals("John Smith", name);
        assertEquals(45, age);
        assertTrue(resultSet.isFirst());
        assertFalse(resultSet.next());
    }

    @DisplayName("Updating single row by primary key")
    @Test
    void testUpdate() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_UPDATE_CUSTOMER);
        statement.setString(1, "Craig Smith");
        statement.setLong(2, 1L);

        int rowsChanged = statement.executeUpdate();

        assertEquals(1, rowsChanged);
    }

    @DisplayName("Deleting single row by primary key")
    @Test
    void testDelete() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(QUERY_DELETE_CUSTOMER);
        statement.setLong(1, 2L);

        int rowsRemoved = statement.executeUpdate();

        assertEquals(1, rowsRemoved);
    }
}
