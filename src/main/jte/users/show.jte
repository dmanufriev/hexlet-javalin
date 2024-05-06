@import org.example.hexlet.model.User
@param User user

@template.layout.page(
content = @`
    @if (user == null)
        <p>Пользователь не найден</p>
    @else
        <h3>${user.getName()}</h3>
        <p>${user.getEmail()}</p>
    @endif
`
)