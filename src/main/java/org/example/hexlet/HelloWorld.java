package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.model.Course;

import java.util.List;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {
    private static final List<Course> courses = List.of(new Course(0, "Java", "Java development"),
                                                new Course(1, "C++", "C++ development"),
                                                new Course(2, "Python", "Python development"));

    public static void main(String[] args) {

        // Создаем приложение
        var app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        // Описываем, что загрузится по адресу /
        app.get("/courses", ctx -> {
            var header = "Programming courses";
            var page = new CoursePage(courses, header);
            ctx.render("index.jte", model("page", page));
        });

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Integer.class).get();
            var course = courses.get(id);
            ctx.render("courses/course.jte", model("course", course));
        });

        app.start(7070); // Стартуем веб-сервер
    }
}
