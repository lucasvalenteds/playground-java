package com.playground.java.modelmapper;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

class MainTest {

    private final ModelMapper mapper = new ModelMapper();

    @DisplayName("It can map a type to another by its fields")
    @Test
    void testNamingConvention() {
        var record = new Record(
            new Person("John Smith", 45),
            List.of(
                new Contract(1, 2500L, false),
                new Contract(2, 500L, true),
                new Contract(3, 100L, false)
            )
        );

        var response = mapper.map(record, ResponseFlattened.class);

        assertEquals("John Smith", response.getPersonName());
        assertEquals(45, response.getPersonAge());
    }

    @DisplayName("It can validate a conversion before attempt to convert")
    @Test
    void testValidation() {
        //var record = new Record(new Person("Mary Doe", 32), List.of());

        assertDoesNotThrow(mapper::validate);
    }
}
