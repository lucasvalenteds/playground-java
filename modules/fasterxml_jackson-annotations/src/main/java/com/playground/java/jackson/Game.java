package com.playground.java.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Game {
    @JsonProperty("id")
    UUID id;

    @JsonProperty
    String name;

    @JsonProperty("max_players")
    Integer players;
}
