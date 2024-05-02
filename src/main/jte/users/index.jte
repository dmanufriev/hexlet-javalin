@import org.example.hexlet.dto.users.UsersPage
@import org.example.hexlet.util.NamedRoutes
@param UsersPage page


@template.layout.page(
content = @`
    <h1>${page.getHeader()}</h1>
    <a href="${NamedRoutes.buildUserPath()}">Добавить нового пользователя</a>
    @if(page.getUsers().isEmpty())
        <p>Список пользователей пуст</p>
    @else
        @for(var user : page.getUsers())
            <div>
                <p>${user.getName()}  ${user.getEmail()}</p>
            </div>
        @endfor
    @endif
`
)
