package exploreWithMe.service;

import exploreWithMe.dto.user.NewUserRequest;
import exploreWithMe.dto.user.UserDto;
import exploreWithMe.exception.NotFoundException;
import exploreWithMe.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static exploreWithMe.mapper.UserMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size, DEFAULT_SORT);
        return toUsersDto(userRepository.getUsersByIds(ids, pageable));
    }

    @Transactional
    public UserDto addUser(NewUserRequest newUserRequest) {
        return toUserDto(userRepository.save(toUser(newUserRequest)));
    }

    @Transactional
    public void deleteUser(Long userId) {
        checkExistUser(userId);
        userRepository.deleteById(userId);
    }
    private void checkExistUser(Long userId){
        if(!userRepository.existsById(userId)){
            throw new NotFoundException("User not found " + userId);
        }
    }
}
