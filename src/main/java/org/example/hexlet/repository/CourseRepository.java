package org.example.hexlet.repository;

import org.example.hexlet.model.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository {
    private static List<Course> entities = new ArrayList<>();

    public static void save(Course course) {
        course.setId((long)entities.size() + 1);
        entities.add(course);
    }

    public static List<Course> search(String term) {

        if (term == null) {
            return entities;
        }

        var courses = entities.stream()
                .filter(entity -> entity.getName().startsWith(term))
                .toList();
        return courses;
    }

    public static Optional<Course> find(Long id) {
        var course = entities.stream()
                .filter(entity -> entity.getId().equals(id))
                .findAny();
        return course;
    }

    public static List<Course> getEntities() {
        return entities;
    }

    public static void delete(Long id) {

    }
}
