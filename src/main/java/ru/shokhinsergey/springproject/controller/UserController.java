//http://localhost:8080/v3/api-docs
// http://localhost:8080/swagger-ui/index.html

package ru.shokhinsergey.springproject.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.shokhinsergey.springproject.dto.UserDtoCreateAndUpdate;
import ru.shokhinsergey.springproject.dto.UserDtoResult;
import ru.shokhinsergey.springproject.exceptionhandler.exception.NotValidArgumentException;
import ru.shokhinsergey.springproject.exceptionhandler.exception.NotValidIdException;
import ru.shokhinsergey.springproject.service.UserService;

import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "UserService API", description = "CRUD операции")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(
            summary = "Поиск \"user\" по \"id\"",
            description = "Возвращает данные пользователя с указанным \"id\" из базы данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя найдены",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDtoResult.class))),
                    @ApiResponse(responseCode = "400", description = "Введенный \"id\" меньше \"1\""),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным \"id\" не найден")
            }
    )
    @GetMapping("/{id}")
    public UserDtoResult get(
            @Parameter(required = true, description = "Идентификатор пользователя", example = "1",
                    schema = @Schema(implementation = Integer.class))
            @PathVariable
            Integer id) {
        if (id <= 0) throw new NotValidIdException();
        Optional<UserDtoResult> optionalResult = userService.get(id);
        return optionalResult.stream().findAny().orElseThrow();
    }


    @Operation(
            summary = "Удаление \"user\" по \"id\"",
            description = "Возвращает данные пользователя с указанным \"id\" после его удаления из базы данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя удалены",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDtoResult.class))),
                    @ApiResponse(responseCode = "400", description = "Введенный \"id\" меньше \"1\""),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным \"id\" не найден")
            }
    )
    @DeleteMapping("/{id}")
    public UserDtoResult delete(
            @Parameter(required = true, description = "Идентификатор пользователя", example = "1",
                    schema = @Schema(implementation = Integer.class))
            @PathVariable
            Integer id) {
        if (id <= 0) throw new NotValidIdException();
        return userService.delete(id).stream().findAny().orElseThrow();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создание \"user\" по введенным данным",
            description = "Возвращает данные нового пользователя после его сохранения в базе данных",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Данные пользователя сохранены",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDtoResult.class))),
                    @ApiResponse(responseCode = "400", description = "Входные данные не прошли валидацию"),
                    @ApiResponse(responseCode = "409", description = "Указанный \"email\" уже сохранен в базе данных")
            }
    )
    public ResponseEntity<UserDtoResult> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Идентификатор пользователя",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoCreateAndUpdate.class)))
            @RequestBody
            @Validated
            UserDtoCreateAndUpdate userCreateDto, BindingResult errors) {
        if (errors.hasErrors()) {
            String message = messageFromErrors(errors);
            throw new NotValidArgumentException(message);
        }
        UserDtoResult result = userService.create(userCreateDto);

        return ResponseEntity.created(Link.of("/users/" + result.getId()).toUri())
                .body(result);
    }

    private String messageFromErrors(BindingResult errors) {
        String lineSeparator = System.lineSeparator();
        return errors.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(lineSeparator));
    }

    @Operation(
            summary = "Обновление \"user\" по \"id\" и введенным данным",
            description = "Возвращает данные пользователя с указанным \"id\" после его обновления в базе данных",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные пользователя обновлены",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = UserDtoResult.class))),
                    @ApiResponse(responseCode = "400", description = "Входные данные не прошли валидацию"),
                    @ApiResponse(responseCode = "404", description = "Пользователь с указанным \"id\" не найден"),
                    @ApiResponse(responseCode = "409", description = "Указанный \"email\" уже сохранен в базе данных")
            }
    )
    @PutMapping("/{id}")
    public UserDtoResult update(
            @Parameter(required = true, description = "Идентификатор пользователя", example = "1",
                    schema = @Schema(implementation = Integer.class))
            @PathVariable
            Integer id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true,
                    description = "Идентификатор пользователя",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserDtoCreateAndUpdate.class)))
            @RequestBody
            @Validated
            UserDtoCreateAndUpdate userCreateDto, BindingResult errors) {
        if (id <= 0) throw new NotValidIdException();
        if (errors.hasErrors()) {
            String message = messageFromErrors(errors);
            throw new NotValidArgumentException(message);
        }
        Optional<UserDtoResult> optionalResult = userService.update(userCreateDto, id);
        return optionalResult.stream().findAny().orElseThrow();
    }
}
