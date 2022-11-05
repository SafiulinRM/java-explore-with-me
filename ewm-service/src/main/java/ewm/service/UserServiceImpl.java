package ewm.service;

import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;
import ewm.exception.NotFoundException;
import ewm.repo.UserRepository;
import ewm.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static ewm.mapper.UserMapper.*;

/**
 * Реализация интерфейса по работе с пользователями {@link UserService}
 *
 * @author safiulinrm
 * @see ewm.model.User
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    /**
     * Сортировка по умолчанию по полю id класса категории
     */
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    /**
     * Сообщение для исключения об отсутствии пользователя в базе данных
     */
    private static final String USER_NOT_FOUND = "User not found ";
    /**
     * Репозиторий для работы с пользователями событий {@link UserRepository}
     */
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return toUsersDto(userRepository.getUsersByIds(ids, pageable));
    }

    @Override
    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        return toUserDto(userRepository.save(toUser(newUserRequest)));
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        checkExistUser(userId);
        userRepository.deleteById(userId);
    }

    /**
     * Проверка на существование пользователя
     *
     * @param userId id пользователя
     */
    private void checkExistUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(USER_NOT_FOUND + userId);
        }
    }
}
