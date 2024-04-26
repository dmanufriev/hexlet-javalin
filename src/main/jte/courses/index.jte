@import org.example.hexlet.dto.courses.CoursePage
@param CoursePage page


@template.layout.page(
content = @`
    <h1>${page.getHeader()}</h1>
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
