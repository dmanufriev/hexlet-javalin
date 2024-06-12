package org.example.hexlet;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import io.javalin.validation.ValidationException;
import org.example.hexlet.controller.CoursesController;
import org.example.hexlet.controller.UsersController;
import org.example.hexlet.dto.MainPage;
import org.example.hexlet.dto.courses.BuildCoursePage;
import org.example.hexlet.model.Course;
import org.example.hexlet.repository.BaseRepository;
import org.example.hexlet.repository.CourseRepository;
import org.example.hexlet.util.NamedRoutes;
import org.example.hexlet.controller.SessionsController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static io.javalin.rendering.template.TemplateUtil.model;

public class HelloWorld {
    private static Javalin app;

    public static void main(String[] args) {

        try {
            initApp();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return;
        }

        rootHandler();
        coursesHandler();
        usersHandler();
        sessionsHandler();

        app.start(7070); // Стартуем веб-сервер
    }
    
    private static void initApp() throws Exception {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:h2:mem:hexlet_project;DB_CLOSE_DELAY=-1");

        var dataSource = new HikariDataSource(hikariConfig);
        // Получаем путь до файла в src/main/resources
        var url = HelloWorld.class.getClassLoader().getResourceAsStream("schema.sql");
        var sql = new BufferedReader(new InputStreamReader(url))
                .lines().collect(Collectors.joining("\n"));

        // Получаем соединение, создаем стейтмент и выполняем запрос
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        app = Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte());
        });
    }

    private static void rootHandler() {
        app.get(NamedRoutes.mainPath(), ctx -> {
            var visited = Boolean.valueOf(ctx.cookie("visited"));
            var page = new MainPage(visited, ctx.sessionAttribute("currentUser"));
            ctx.render("index.jte", model("page", page));
            ctx.cookie("visited", String.valueOf(true));
        });
    }

    private static void coursesHandler() {
        app.get(NamedRoutes.coursesPath(), CoursesController::index);
        app.get(NamedRoutes.buildCoursePath(), CoursesController::buildForm);
        app.post(NamedRoutes.coursesPath(), CoursesController::create);
        app.get(NamedRoutes.coursePath("{id}"), CoursesController::show);
        app.get(NamedRoutes.editCoursePath("{id}"), CoursesController::editForm);
        app.patch(NamedRoutes.coursePath("{id}"), CoursesController::update);
        app.delete(NamedRoutes.coursePath("{id}"), CoursesController::destroy);
    }

    private static void usersHandler() {
        app.get(NamedRoutes.usersPath(), UsersController::index);
        app.get(NamedRoutes.buildUserPath(), UsersController::buildForm);
        app.post(NamedRoutes.usersPath(), UsersController::create);
        app.get(NamedRoutes.userPath("{id}"), UsersController::show);
        app.get(NamedRoutes.editUserPath("{id}"), UsersController::editForm);
        app.patch(NamedRoutes.userPath("{id}"), UsersController::update);
        app.delete(NamedRoutes.userPath("{id}"), UsersController::destroy);
    }

    private static void sessionsHandler() {
        // Отображение формы логина
        app.get(NamedRoutes.buildSessionPath(), SessionsController::build);
        // Процесс логина
        app.post(NamedRoutes.sessionsPath(), SessionsController::create);
        // Процесс выхода из аккаунта
        app.delete(NamedRoutes.sessionsPath(), SessionsController::destroy);
    }
}
