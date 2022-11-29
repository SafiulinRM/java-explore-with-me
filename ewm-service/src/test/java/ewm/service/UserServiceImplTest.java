package ewm.service;

import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;
import ewm.exception.NotFoundException;
import ewm.model.User;
import ewm.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceImplTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userService;
    private final UserDto testUserDto = new UserDto("user1@mail.com", 1L, "user");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final NewUserRequest testNewUserDto = new NewUserRequest("user1@mail.com", "user");

    @Test
    void getUsers() {
        when(userRepository.getUsersByIds(any(), any(PageRequest.class))).thenReturn(new PageImpl(List.of(testUser)));
        val usersDto = userService.getUsers(List.of(1L), 0, 10);
        assertEquals(usersDto.get(0), testUserDto, "Пользователи не получены");
    }

    @Test
    void addUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        val userDto = userService.addUser(testNewUserDto);
        assertEquals(userDto, testUserDto, "Пользователь не создался");
    }

    @Test
    void deleteUser() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
        verify(userRepository, times(1)).existsById(1L);
        when(userRepository.existsById(anyLong())).thenReturn(false);
        val exception = assertThrows(NotFoundException.class,
                () -> userService.deleteUser(1L));
        assertEquals("User not found 1", exception.getMessage());
    }
}