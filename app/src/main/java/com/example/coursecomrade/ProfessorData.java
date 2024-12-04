package com.example.coursecomrade;

import java.util.Objects;

public class ProfessorData {
    private final String professorId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String rating;

    // Constructor
    public ProfessorData(String professorId, String firstName, String lastName, String email, String rating) {
        this.professorId = professorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.rating = rating;
    }

    // Getters
    public String getProfessorId() {
        return professorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getRating() {
        return rating;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfessorData that = (ProfessorData) o;
        return Objects.equals(professorId, that.professorId) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(lastName, that.lastName) &&
                Objects.equals(email, that.email) &&
                Objects.equals(rating, that.rating);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(professorId, firstName, lastName, email, rating);
    }

    // toString method
    @Override
    public String toString() {
        return "ProfessorData{" +
                "professorId='" + professorId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
