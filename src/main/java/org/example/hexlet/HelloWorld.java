package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.controller.CoursesController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {
    private static Javalin app;

    public static void main(String[] args) {

        // Создаем приложение
        app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });

        rootHandler();
        coursesHandler();
        usersHandler();

        app.start(7070); // Стартуем веб-сервер
    }

    private static void rootHandler() {
        app.get(NamedRoutes.mainPath(), ctx -> {
            //var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage(false, ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", model("page", page));
            //ctx.cookie("visited", String.valueOf(true));
        });
    }

    private static void coursesHandler() {
        app.get(NamedRoutes.coursesPath(), CoursesController::index);
        app.get(NamedRoutes.buildCoursePath(), CoursesController::buildForm);
        app.post(NamedRoutes.coursesPath(), CoursesController::create);
        app.get(NamedRoutes.coursePath("{id}"), CoursesController::show);
        app.get(NamedRoutes.editCoursePath("{id}"), CoursesController::editForm);
        app.patch(NamedRoutes.updateCoursePath("{id}"), CoursesController::update);
        app.delete(NamedRoutes.destroyCoursePath("{id}"), CoursesController::destroy);
    }

    private static void usersHandler() {
        app.get(NamedRoutes.usersPath(), UsersController::index);
        app.get(NamedRoutes.buildUserPath(), UsersController::buildForm);
        app.post(NamedRoutes.usersPath(), UsersController::create);
        app.get(NamedRoutes.userPath("{id}"), UsersController::show);
        app.get(NamedRoutes.editUserPath("{id}"), UsersController::editForm);
        app.patch(NamedRoutes.updateUserPath("{id}"), UsersController::update);
        app.delete(NamedRoutes.destroyUserPath("{id}"), UsersController::destroy);
    }
}
