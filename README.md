EndPoints:
1) Find user by id:
@GetMapping("/users/{id}")
public UserDtoResult get(@PathVariable Integer id)

2) Delete user by id
@DeleteMapping("/users/{id}")
public UserDtoResult delete(@PathVariable Integer id)

3) Create user
@PostMapping("/users")
public UserDtoResult create(UserDtoCreateAndUpdate userCreateDto)

4) Update user
@PutMapping("/users/{id}")
public UserDtoResult update(Integer id, UserDtoCreateAndUpdate userUpdateDto)

UserDtoCreateAndUpdate создается на основе Json строки. Пример: {"name":"Example", "email":"example@gmail.com", "age":99}.

UserDtoResult представлен Json строкой. Пример: {"id":111, "name":"Example", "email":"example@gmail.com", "age":99, "created_at":"30-12-2000"}.

Отправка запросов производилась через PostMan.

Для приложения порт 8080.

Задание:
Добавить в user-service поддержку Spring и разработать API, которое позволит управлять данными.

Использовать необходимые модули spring(boot, web, data etc).
Реализовать api для получения, создания, обновления и удаления юзера. Важно, entity не должен возвращаться из контроллера, необходимо использовать dto.
Заменить Hibernate на Spring data JPA.
Написать тесты для API(можно делать это при помощи mockMvc или других средств)

Задание
Добавление Swagger-документации и HATEOAS в API.

Задокументировать существующее API (из задания 4) с помощью Swagger (Springdoc OpenAPI), чтобы можно было легко изучить и тестировать API через веб-интерфейс.
Добавить поддержку HATEOAS, чтобы API предоставляло ссылки для навигации по ресурсам
