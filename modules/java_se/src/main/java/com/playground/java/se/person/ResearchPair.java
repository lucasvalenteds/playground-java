package com.playground.java.se.person;

import java.util.Objects;

public class ResearchPair {

    private final Person person;
    private final Score score;

    public ResearchPair(Person person, Score score) {
        this.person = person;
        this.score = score;
    }

    public Person getPerson() {
        return person;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResearchPair that = (ResearchPair) o;
        return Objects.equals(person, that.person) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {

        return Objects.hash(person, score);
    }

    @Override
    public String toString() {
        return "ResearchPair{" +
                "person=" + person +
                ", score=" + score +
                '}';
    }
}
