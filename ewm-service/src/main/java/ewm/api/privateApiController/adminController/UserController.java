package ewm.api.privateApiController.adminController;

import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;
import ewm.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Закрытый контроллер пользователей ({@link ewm.model.User})
 * для администратора
 *
 * @author safiulinrm
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    /**
     * Сервис для работы с событиями {@link UserService}
     */
    private final UserService userService;

    /**
     * Возвращает информацию обо всех пользователях (учитываются параметры ограничения выборки),
     * либо о конкретных (учитываются указанные идентификаторы)
     *
     * @param ids  id пользователей
     * @param from количество элементов, которые нужно пропустить для формирования
     *             текущего набора Default value : 0
     * @param size количество элементов в наборе
     *             Default value : 10
     * @return Возвращает список полной информации о пользователях {@link List {@link UserDto}}
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        var usersDto = userService.getUsers(ids, from, size);
        log.info("Пользователи найдены");
        return usersDto;
    }

    /**
     * Добавление нового пользователя
     *
     * @param newUserRequest данные добавляемого пользователя {@link NewUserRequest}
     * @return возвращает нового пользователя {@link UserDto}
     */
    @PostMapping
    public UserDto addUser(@RequestBody @Validated NewUserRequest newUserRequest) {
        var userDto = userService.addUser(newUserRequest);
        log.info("Пользователь зарегистрирован");
        return userDto;
    }

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     */
    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь удален");
    }
}