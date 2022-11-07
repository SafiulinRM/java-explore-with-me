package ewm.service.interfaces;

import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;

import java.util.List;

/**
 * Интерфейс сервиса обработки запросов по пользователям
 */
public interface UserService {
    /**
     * Получение пользователей с учетом пагинации
     *
     * @param ids  ids пользователей
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество элементов в наборе
     *             Default value : 10
     * @return список пользователей {@link List {@link UserDto}}
     */
    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    /**
     * Сохранение нового пользователя
     *
     * @param newUserRequest данные нового пользователя {@link NewUserRequest}
     * @return сохраненный пользователь {@link UserDto}
     */
    UserDto addUser(NewUserRequest newUserRequest);

    /**
     * Удаление пользователя
     *
     * @param userId id пользователя
     */
    void deleteUser(Long userId);
}
