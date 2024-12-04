package com.example.coursecomrade;

import java.util.Objects;

public class ClassData {
    private final String classId;
    private final String className;
    private final String classCode;
    private final String description;

    // Constructor
    public ClassData(String classId, String className, String classCode, String description) {
        this.classId = classId;
        this.className = className;
        this.classCode = classCode;
        this.description = description;
    }

    // Getters
    public String getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getDescription() {
        return description;
    }

    // equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassData classData = (ClassData) o;
        return Objects.equals(classId, classData.classId) &&
                Objects.equals(className, classData.className) &&
                Objects.equals(classCode, classData.classCode) &&
                Objects.equals(description, classData.description);
    }

    // hashCode method
    @Override
    public int hashCode() {
        return Objects.hash(classId, className, classCode, description);
    }

    // toString method
    @Override
    public String toString() {
        return "ClassData{" +
                "classId='" + classId + '\'' +
                ", className='" + className + '\'' +
                ", classCode='" + classCode + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
