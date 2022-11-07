package ewm.mapper;

import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;
import ewm.dto.user.UserShortDto;
import ewm.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Класс, с помощью которого можно преобразовать пользователя в dto и наоборот
 *
 * @author safiulinrm
 * @see User
 * @see UserDto
 * @see NewUserRequest
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserMapper {
    /**
     * Преобразование пользователя в dto
     *
     * @param user данные пользователя {@link User}
     * @return {@link UserDto}
     */
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getEmail(),
                user.getId(),
                user.getName()
        );
    }

    /**
     * Преобразование нового пользователя в класс хранения
     *
     * @param newUserRequest данные нового пользователя {@link NewUserRequest}
     * @return {@link User}
     */
    public static User toUser(NewUserRequest newUserRequest) {
        User user = new User();
        user.setName(newUserRequest.getName());
        user.setEmail(newUserRequest.getEmail());
        return user;
    }

    /**
     * Преобразование Пользователя в shortDto
     *
     * @param user данные пользователя {@link User}
     * @return {@link UserShortDto}
     */
    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    /**
     * Преобразование пользователей в список dto
     *
     * @param users данные пользователей {@link User}
     * @return {@link List {@link UserDto}}
     */
    public static List<UserDto> toUsersDto(Page<User> users) {
        return users.stream()
                .map(UserMapper::toUserDto)
                .collect(toList());
    }
}
