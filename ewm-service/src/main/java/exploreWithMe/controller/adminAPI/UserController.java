package exploreWithMe.controller.adminAPI;

import exploreWithMe.dto.user.NewUserRequest;
import exploreWithMe.dto.user.UserDto;
import exploreWithMe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        var usersDto = userService.getUsers(ids, from, size);
        log.info("Пользователи найдены");
        return usersDto;
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Validated NewUserRequest newUserRequest) {
        var userDto = userService.addUser(newUserRequest);
        log.info("Пользователь зарегистрирован");
        return userDto;
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        log.info("Пользователь удален");
    }
}