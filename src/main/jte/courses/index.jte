@import org.example.hexlet.dto.courses.CoursePage
@import org.example.hexlet.util.NamedRoutes
@param CoursePage page


@template.layout.page(
content = @`
    <h1>${page.getHeader()}</h1>
    <a href="/courses/build">Добавить новый курс</a>
    <form action="/courses" method="get">
        <input type="search" name="term" value="${page.getTerm()}" />
        <input type="date" name="date" />
        <input type="time" name="time" />
        <input type="datetime-local" name="dateTime" />
        <input type="submit" value="Search" />
    </form>
    @if(page.getCourses().isEmpty())
        <p>No courses found</p>
    @else
        @for(var course : page.getCourses())
            <div>
                <h2><a href="/courses/${course.getId()}">${course.getName()}</a></h2>
                <p>${course.getDescription()}</p>
            </div>
        @endfor
    @endif
`
)
