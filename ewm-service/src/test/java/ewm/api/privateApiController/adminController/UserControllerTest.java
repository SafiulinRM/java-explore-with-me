package ewm.api.privateApiController.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.user.NewUserRequest;
import ewm.dto.user.UserDto;
import ewm.service.interfaces.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mvc;
    private final UserDto testUserDto = new UserDto("user1@mail.com", 1L, "user");
    private final NewUserRequest testNewUserDto = new NewUserRequest("user1@mail.com", "user");

    @Test
    void getUsers() throws Exception {
        when(userService.getUsers(anyList(), anyInt(), anyInt())).thenReturn(List.of(testUserDto));
        mvc.perform(get("/admin/users?ids=1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testUserDto))));
    }

    @Test
    void addUser() throws Exception {
        when(userService.addUser(any(NewUserRequest.class))).thenReturn(testUserDto);
        mvc.perform(post("/admin/users")
                        .content(mapper.writeValueAsString(testNewUserDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testUserDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(testUserDto.getName())))
                .andExpect(jsonPath("$.email", is(testUserDto.getEmail())));
    }

    @Test
    void deleteUser() throws Exception {
        mvc.perform(delete("/admin/users/1"));
        Mockito.verify(userService, Mockito.times(1))
                .deleteUser(1L);
    }
}