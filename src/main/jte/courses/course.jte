@import org.example.hexlet.model.Course
@param Course course

@template.layout.page(
    content = @`
        <h1>${course.getName()}</h1>
        <p>${course.getDescription()}</p>
    `
)
