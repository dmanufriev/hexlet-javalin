package org.example.hexlet.dto.courses;

import org.example.hexlet.model.Course;
import java.util.List;

public final class CoursePage {
    private List<Course> courses;
    private String header;

    public CoursePage(List<Course> courses, String header) {
        this.courses = courses;
        this.header = header;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public String getHeader() {
        return header;
    }
}