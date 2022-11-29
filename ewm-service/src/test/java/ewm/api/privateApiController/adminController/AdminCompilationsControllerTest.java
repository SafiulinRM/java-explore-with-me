package ewm.api.privateApiController.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.category.CategoryDto;
import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.user.UserShortDto;
import ewm.service.interfaces.CompilationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminCompilationsController.class)
class AdminCompilationsControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private CompilationService compilationService;
    @Autowired
    private MockMvc mvc;
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final UserShortDto testShortUserDto = new UserShortDto(1L, "user");
    private final EventShortDto testEventShortDto = new EventShortDto(
            "annotation",
            testCategoryDto,
            10L,
            LocalDateTime.now().plusHours(4),
            1L,
            testShortUserDto,
            true,
            "title",
            1L);
    private final NewCompilationDto testNewCompilation = new NewCompilationDto(
            List.of(1L),
            true,
            "title");
    private final CompilationDto testCompilationDto = new CompilationDto(
            List.of(testEventShortDto),
            1L,
            true,
            "title");

    @Test
    void addCompilation() throws Exception {
        when(compilationService.addCompilation(any(NewCompilationDto.class)))
                .thenReturn(testCompilationDto);
        mvc.perform(post("/admin/compilations")
                        .content(mapper.writeValueAsString(testNewCompilation))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testCompilationDto)));
    }

    @Test
    void deleteCompilation() throws Exception {
        mvc.perform(delete("/admin/compilations/1"));
        Mockito.verify(compilationService, Mockito.times(1))
                .deleteCompilation(1L);
    }

    @Test
    void deleteEventOfCompilation() throws Exception {
        mvc.perform(delete("/admin/compilations/1/events/1"));
        Mockito.verify(compilationService, Mockito.times(1))
                .deleteEventOfCompilation(1L, 1L);
    }

    @Test
    void addEventOfCompilation() throws Exception {
        mvc.perform(patch("/admin/compilations/1/events/1"));
        Mockito.verify(compilationService, Mockito.times(1))
                .addEventOfCompilation(1L, 1L);
    }

    @Test
    void unpinCompilation() throws Exception {
        mvc.perform(delete("/admin/compilations/1/pin"));
        Mockito.verify(compilationService, Mockito.times(1))
                .unpinCompilation(1L);
    }

    @Test
    void pinCompilation() throws Exception {
        mvc.perform(patch("/admin/compilations/1/pin"));
        Mockito.verify(compilationService, Mockito.times(1))
                .pinCompilation(1L);
    }
}