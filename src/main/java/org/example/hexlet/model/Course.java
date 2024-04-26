package org.example.hexlet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public final class Course {
    private int id;
    private String name;
    private String description;
}
