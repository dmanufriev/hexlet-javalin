@import org.example.hexlet.util.NamedRoutes
@import org.example.hexlet.dto.courses.BuildCoursePage
@param BuildCoursePage page

@template.layout.page(
content = @`
    @if(page.getErrors() != null)
        <ul>
            @for(var validator : page.getErrors().values())
                @for(var error : validator)
                    <div class="alert alert-danger" role="alert">
                        ${error.getMessage()}
                    </div>
                @endfor
            @endfor
        </ul>
    @endif

    <form action="${NamedRoutes.coursesPath()}" method="post">
        <div>
            <label>
                Название
                <input type="text" required name="name" value="${page.getName()}" />
            </label>
        </div>
        <div>
            <label>
                Описание
                <input type="text" required name="description" value="${page.getDescription()}" />
            </label>
        </div>
        <input type="submit" value="Добавить" />
    </form>
`
)
