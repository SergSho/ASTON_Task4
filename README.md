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
