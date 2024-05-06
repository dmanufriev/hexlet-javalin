package org.example.hexlet.controller;

import io.javalin.http.NotFoundResponse;
import io.javalin.validation.ValidationException;
import org.example.hexlet.dto.users.BuildUserPage;
import org.example.hexlet.dto.users.UserPage;
import org.example.hexlet.dto.users.UsersPage;
import org.example.hexlet.model.User;
import org.example.hexlet.repository.UserRepository;
import io.javalin.http.Context;
import org.example.hexlet.util.NamedRoutes;

import static io.javalin.rendering.template.TemplateUtil.model;

public class UsersController {
    // Список пользователей
    public static void index (Context ctx) {
        var header = "Пользователи";
        var users = UserRepository.getEntities();
        var page = new UsersPage(users, header);
        ctx.render("users/index.jte", model("page", page));
    }

    // Профиль пользователя
    public static void show (Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var user = UserRepository.find(id).get();
        ctx.render("users/show.jte", model("user", user));
    }

    // Форма создания нового пользователя
    public static void buildForm (Context ctx) {
        var page = new BuildUserPage();
        ctx.render("users/build.jte", model("page", page));
    }

    // Создание нового пользователя
    public static void create (Context ctx) {
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
    }

    // Форма редактирования профиля пользователя
    public static void editForm (Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        var user = UserRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        var page = new UserPage(user);
        ctx.render("users/edit.jte", model("page", page));
    }

    // Обновление профиля пользователя
    public static void update (Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();

        var name = ctx.formParam("name");
        var email = ctx.formParam("email");
        var password = ctx.formParam("password");

        var user = UserRepository.find(id)
                .orElseThrow(() -> new NotFoundResponse("Entity with id = " + id + " not found"));
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        UserRepository.save(user);
        ctx.redirect(NamedRoutes.usersPath());
    }

    // Удаление профиля пользователя
    public static void destroy (Context ctx) {
        var id = ctx.pathParamAsClass("id", Long.class).get();
        UserRepository.delete(id);
        ctx.redirect(NamedRoutes.usersPath());
    }
}
