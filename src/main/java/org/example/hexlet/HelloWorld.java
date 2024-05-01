package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.dto.MainPage;
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
        app.get("/", ctx -> {
            //var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage(false, ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", model("page", page));
            //ctx.cookie("visited", String.valueOf(true));
        });

        app.get("/courses", ctx -> {
            var header = "Programming courses";
            var term = ctx.queryParam("term");
            List<Course> coursesList = courses;
            if (term != null) {
                coursesList = courses.stream()
                                    .filter(course -> course.getName().toLowerCase().contains(term.toLowerCase()))
                                    .toList();
            }
            var page = new CoursePage(coursesList, header, term);
            ctx.render("courses/index.jte", model("page", page));
        });

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Integer.class).get();
            var course = courses.get(id);
            ctx.render("courses/course.jte", model("course", course));
        });

        app.start(7070); // Стартуем веб-сервер
    }
}
