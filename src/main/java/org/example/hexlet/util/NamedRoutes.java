package org.example.hexlet.util;

/**
 * NamedRoutes.
 */
public class NamedRoutes {

    public static String mainPath() {
        return "/";
    }

    // Sessions
    public static String sessionsPath() {
        return "/sessions";
    }
    public static String buildSessionPath() {
        return "/sessions/build";
    }

    // Users
    public static String usersPath() {
        return "/users";
    }
    public static String buildUserPath() {
        return "/users/build";
    }
    public static String userPath(Long id) {
        return userPath(String.valueOf(id));
    }
    public static String userPath(String id) {
        return "/users/" + id;
    }
    public static String editUserPath(String id) { return userPath(id) + "/edit"; }
    public static String updateUserPath(String id) { return userPath(id) + "/update"; }
    public static String destroyUserPath(String id) { return userPath(id) + "/destroy"; }

    // Courses
    public static String buildCoursePath() {
        return "/courses/build";
    }
    public static String coursesPath() {
        return "/courses";
    }
    public static String coursePath(Long id) {
        return coursePath(String.valueOf(id));
    }
    public static String coursePath(String id) {
        return "/courses/" + id;
    }
    public static String editCoursePath(String id) { return coursePath(id) + "/edit"; }
    public static String updateCoursePath(String id) { return coursePath(id) + "/update"; }
    public static String destroyCoursePath(String id) { return coursePath(id) + "/destroy"; }

    // Github
    public static String githubPath() {
        return "https://github.com/dmanufriev";
    }
}