package org.example.hexlet;

import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.courses.CoursePage;
import org.example.hexlet.dto.users.UsersPage;
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
        app.get("/", ctx -> {
            //var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage(false, ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", model("page", page));
            //ctx.cookie("visited", String.valueOf(true));
        });
    }

    private static void coursesHandler() {
        app.get("/courses", ctx -> {
            var header = "Programming courses";
            var term = ctx.queryParam("term");
            var coursesList = CourseRepository.search(term);
            var page = new CoursePage(coursesList, header, term);
            ctx.render("courses/index.jte", model("page", page));
        });

        app.get("/courses/build", ctx -> ctx.render("courses/build.jte"));

        app.post("/courses", ctx -> {
            var name = ctx.formParam("name").trim();
            var description = ctx.formParam("description");
            var course = new Course(name, description);
            CourseRepository.save(course);
            ctx.redirect(NamedRoutes.coursesPath());
        });

        app.get("/courses/{id}", ctx -> {
            var id = ctx.pathParamAsClass("id", Integer.class).get();
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
            ctx.render("users/build.jte");
        });

        app.post(NamedRoutes.usersPath(), ctx -> {
            var name = ctx.formParam("name").trim();
            var email = ctx.formParam("email").trim().toLowerCase();
            var password = ctx.formParam("password");
            var passwordConfirmation = ctx.formParam("passwordConfirmation");

            var user = new User(name, email, password);
            UserRepository.save(user);
            ctx.redirect(NamedRoutes.usersPath());
        });
    }
}
