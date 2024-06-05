package org.example.hexlet.dto.courses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.hexlet.dto.BasePage;
import org.example.hexlet.model.Course;

@AllArgsConstructor
@Getter
public class CoursePage extends BasePage {
    private Course course;
}
