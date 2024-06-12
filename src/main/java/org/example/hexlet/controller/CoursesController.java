package org.example.hexlet.controller;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.courses.CoursesPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.util.NamedRoutes;

import java.sql.SQLException;

import static io.javalin.rendering.template.TemplateUtil.model;

public class CoursesController {
    // Список курсов
    public static void index(Context ctx) throws SQLException {
        var header = "Programming courses";
        var coursesList = CourseRepository.getEntities();
        var page = new CoursesPage(coursesList, header, "");
        page.setFlash(ctx.consumeSessionAttribute("flash"));
        page.setFlashStyle(ctx.consumeSessionAttribute("flashStyle"));
        ctx.render("courses/index.jte", model("page", page));
    }

    // Страница курса
    public static void show(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var course = CourseRepository.find(id).get();
        ctx.render("courses/show.jte", model("course", course));
    }

    // Форма создания нового курса
    public static void buildForm(Context ctx) {
        var page = new BuildCoursePage();
        ctx.render("courses/build.jte", model("page", page));
    }

    // Создание нового курса
    public static void create(Context ctx) throws SQLException {
        try {
            var name = ctx.formParamAsClass("name", String.class)
                    .check(value -> value.length() > 2, "Имя должно быть длиннее 2")
                    .get().trim();
            var description = ctx.formParamAsClass("description", String.class)
                    .check(value -> value.length() > 10, "Описание должно быть длиннее 10")
                    .get();
            var course = new Course(name, description);
            CourseRepository.save(course);
            ctx.sessionAttribute("flash", "Курс \"" + name + "\" успешно создан!");
            ctx.sessionAttribute("flashStyle", "alert-success");
            ctx.redirect(NamedRoutes.coursesPath());
        } catch (ValidationException e) {
            var page = new BuildCoursePage(ctx.formParam("name"), ctx.formParam("description"), e.getErrors());
            ctx.render("courses/build.jte", model("page", page));
        }
    }

    // Форма редактирования курса
    public static void editForm(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var course = CourseRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new CoursePage(course);
        ctx.render("courses/edit.jte", model("page", page));
    }

    // Обновление страницы курса
    public static void update(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var name = ctx.formParam("name");
        var description = ctx.formParam("description");
        var course = CourseRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        course.setName(name);
        course.setDescription(description);
        CourseRepository.save(course);
        ctx.redirect(NamedRoutes.coursesPath());
    }

    // Удаление курса
    public static void destroy(Context ctx) throws SQLException {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        CourseRepository.delete(id);
        ctx.redirect(NamedRoutes.coursesPath());
    }
}
