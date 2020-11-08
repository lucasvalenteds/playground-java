package com.playground.java.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class MainTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @DisplayName("Does not throw exception if ignoreUnknown is true and payload contains unknown property")
    @Test
    void testIgnoreUnknownTrue() {
        var payloadWithUnknownFieldAge = "{\"name\":\"John Smith\",\"age\":45}";

        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(payloadWithUnknownFieldAge, Person.class));
    }

    @DisplayName("Throws exception if ignoreUnknown is false and payload contains unknown property")
    @Test
    void testIgnoreUnknownFalse() {
        var payloadWithUnknownFieldBrand = "{\"model\":\"Focus\",\"brand\":\"Ford\"}";

        assertThrows(UnrecognizedPropertyException.class, () ->
            objectMapper.readValue(payloadWithUnknownFieldBrand, Car.class)
        );
    }

    @DisplayName("It throws exception by default if ignoreUnknown is not set")
    @Test
    void testClassWithoutTheAnnotation() {
        var payloadWithUnknownFieldWeight = "{\"color\":\"yellow\",\"weight\":2.5}";

        assertThrows(UnrecognizedPropertyException.class, () ->
            objectMapper.readValue(payloadWithUnknownFieldWeight, Fruit.class)
        );
    }

    @DisplayName("Renaming a JSON attribute on deserialization")
    @Test
    void testRenamingProperty() {
        var payload = "{\"id\":\"ac336a6a-1ab2-11eb-9b34-6f949b3cda78\",\"name\":\"Minecraft\",\"max_players\":1}";

        assertDoesNotThrow(() -> {
            Game game = objectMapper.readValue(payload, Game.class);

            assertEquals(1, game.players);
        });
    }

    @DisplayName("Serializing and Deserializing field of type UUID")
    @Test
    void testPropertyUUID() {
        var payload = "{\"id\":\"ac336a6a-1ab2-11eb-9b34-6f949b3cda78\",\"name\":\"Minecraft\",\"max_players\":1}";

        assertDoesNotThrow(() -> {
            Game game = objectMapper.readValue(payload, Game.class);

            assertNotNull(game.id);
            assertEquals(UUID.class, game.id.getClass());
        });
    }
}
