@import org.example.hexlet.model.Course
@param Course course

@template.layout.page(
    content = @`
        @if (course == null)
            <p>Курс не найден</p>
        @else
            <h1>${course.getName()}</h1>
            <p>${course.getDescription()}</p>
        @endif
    `
)
