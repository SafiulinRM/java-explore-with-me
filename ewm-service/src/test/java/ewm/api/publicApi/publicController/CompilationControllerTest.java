package ewm.api.publicApi.publicController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.category.CategoryDto;
import ewm.dto.compilation.CompilationDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.user.UserShortDto;
import ewm.model.*;
import ewm.service.interfaces.CompilationService;
import ewm.util.status.EventState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CompilationController.class)
class CompilationControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CompilationService compilationService;
    @Autowired
    private MockMvc mvc;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");
    private final UserShortDto testShortUserDto = new UserShortDto(1L, "user");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final Comment testComment = new Comment("comment", testUser);
    private final Location testLocation = new Location(1L, (float) 64.6, (float) 64.67);
    private final Event testEvent = new Event(
            List.of(testComment),
            "annotation",
            testCategory,
            10L,
            LocalDateTime.now(),
            "description",
            LocalDateTime.now().plusHours(4),
            1L,
            testUser,
            testLocation,
            true,
            12L,
            LocalDateTime.now().plusHours(1),
            true,
            EventState.PUBLISHED,
            "title",
            1L);
    private final EventShortDto testEventShortDto = new EventShortDto("annotation",
            testCategoryDto,
            10L,
            testEvent.getEventDate(),
            1L,
            testShortUserDto,
            true,
            "title",
            1L);
    private final CompilationDto testCompilationDto = new CompilationDto(
            List.of(testEventShortDto),
            1L,
            true,
            "title");

    @Test
    void getCompilations() throws Exception {
        when(compilationService.getCompilationsByPinned(any(), anyInt(), anyInt())).thenReturn(List.of(testCompilationDto));
        mvc.perform(get("/compilations"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testCompilationDto))));
    }

    @Test
    void getCompilation() throws Exception {
        when(compilationService.getCompilation(anyLong())).thenReturn(testCompilationDto);
        mvc.perform(get("/compilations/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCompilationDto)));
    }
}