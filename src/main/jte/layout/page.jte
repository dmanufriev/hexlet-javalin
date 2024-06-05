@import org.example.hexlet.util.NamedRoutes
@import org.example.hexlet.dto.BasePage
@import gg.jte.Content

@param Content content
@param BasePage page = null

<!doctype html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.0.0/dist/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
        <title>Hexlet Javalin Example</title>
    </head>
    <body>
        @if(page != null && page.getFlash() != null)
            <div class="alert ${page.getFlashStyle()}" role="alert">
                ${page.getFlash()}
            </div>
        @endif
        <div class="menu">
            <p>
                <a href="${NamedRoutes.mainPath()}">Main</a>
                <a href="${NamedRoutes.usersPath()}">Users</a>
                <a href="${NamedRoutes.coursesPath()}">Courses</a>
                <a href="${NamedRoutes.buildSessionPath()}">Sign in</a>
            </p>
        </div>
        ${content}
        <div class="footer">
            <a href="${NamedRoutes.githubPath()}">GitHub профиль</a>
        </div>
    </body>
</html>
