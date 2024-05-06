package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.model.Course;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.repository.UserRepository;
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
        app.get(NamedRoutes.coursesPath(), ctx -> {
            var header = "Programming courses";
            var term = ctx.queryParam("term");
            var coursesList = CourseRepository.search(term);
            var page = new CoursePage(coursesList, header, term);
            ctx.render("courses/index.jte", model("page", page));
        });

        app.get(NamedRoutes.buildCoursePath(), ctx -> {
            var page = new BuildCoursePage();
            ctx.render("courses/build.jte", model("page", page));
        });

        app.post(NamedRoutes.coursesPath(), ctx -> {
            try {
                var name = ctx.formParamAsClass("name", String.class)
                        .check(value -> value.length() > 2, "Имя должно быть длиннее 2")
                        .get().trim();
                var description = ctx.formParamAsClass("description", String.class)
                        .check(value -> value.length() > 10, "Описание должно быть длиннее 10")
                        .get();
                var course = new Course(name, description);
                CourseRepository.save(course);
                ctx.redirect(NamedRoutes.coursesPath());
            } catch (ValidationException e) {
                var page = new BuildCoursePage(ctx.formParam("name"), ctx.formParam("description"), e.getErrors());
                ctx.render("courses/build.jte", model("page", page));
            }
        });

        app.get(NamedRoutes.coursePath("{id}"), ctx -> {
            var id = ctx.pathParamAsClass("id", Long.class).get();
            var course = CourseRepository.find(id).get();
            ctx.render("courses/course.jte", model("course", course));
        });
    }

    private static void usersHandler() {

        app.get(NamedRoutes.usersPath(), ctx -> {
            var header = "Пользователи";
            var users = UserRepository.getEntities();
            var page = new UsersPage(users, header);
            ctx.render("users/index.jte", model("page", page));
        });

        app.get(NamedRoutes.buildUserPath(), ctx -> {
            var page = new BuildUserPage();
            ctx.render("users/build.jte", model("page", page));
        });

        app.post(NamedRoutes.usersPath(), ctx -> {
            var name = ctx.formParam("name").trim();
            var email = ctx.formParam("email").trim().toLowerCase();
            try {
                var passwordConfirmation = ctx.formParam("passwordConfirmation");
                var password = ctx.formParamAsClass("password", String.class)
                        .check(value -> value.equals(passwordConfirmation), "Пароли не совпадают")
                        .check(value -> value.length() > 6, "У пароля недостаточная длина")
                        .get();
                var user = new User(name, email, password);
                UserRepository.save(user);
                ctx.redirect(NamedRoutes.usersPath());
            } catch (ValidationException e) {
                var page = new BuildUserPage(name, email, e.getErrors());
                ctx.render("users/build.jte", model("page", page));
            }
        });
    }
}
