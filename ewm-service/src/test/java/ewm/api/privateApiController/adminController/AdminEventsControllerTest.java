package ewm.api.privateApiController.adminController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.LocationShort;
import ewm.dto.category.CategoryDto;
import ewm.dto.comment.CommentDto;
import ewm.dto.event.AdminUpdateEventRequest;
import ewm.dto.event.EventFullDto;
import ewm.dto.user.UserShortDto;
import ewm.model.*;
import ewm.service.interfaces.EventService;
import ewm.util.filter.EventAdminFilter;
import ewm.util.status.EventState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminEventsController.class)
class AdminEventsControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private EventService eventService;
    @Autowired
    private MockMvc mvc;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final UserShortDto testUserShortDto = new UserShortDto(1L, "user");
    private final Comment testComment = new Comment("comment", testUser);
    private final CommentDto testCommentDto = new CommentDto(
            "comment",
            testUser.getName(),
            testComment.getCreatedOn());
    private final LocationShort testLocationDto =
            new LocationShort((float) 64.6, (float) 64.67);
    private final Location testLocation =
            new Location(1L, (float) 64.6, (float) 64.67);
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
    private final EventFullDto testEventFullDto = new EventFullDto(
            List.of(testCommentDto),
            "annotation",
            testCategoryDto,
            10L,
            testEvent.getCreatedOn(),
            "description",
            testEvent.getEventDate().format(FORMATTER),
            1L,
            testUserShortDto,
            testLocation,
            true,
            12L,
            testEvent.getPublishedOn(),
            true,
            EventState.PUBLISHED.toString(),
            "title",
            1L);
    private final AdminUpdateEventRequest testAdminUpdateEventRequest = new AdminUpdateEventRequest(
            "annotation",
            1L,
            "description",
            testEvent.getEventDate().format(FORMATTER),
            testLocationDto,
            true,
            12L,
            true,
            "title"
    );

    @Test
    void getEvents() throws Exception {
        when(eventService.getEvents(any(EventAdminFilter.class)))
                .thenReturn(List.of(testEventFullDto));
        mvc.perform(get("/admin/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testEventFullDto))));
    }

    @Test
    void putEvent() throws Exception {
        when(eventService.putEvent(anyLong(), any(AdminUpdateEventRequest.class)))
                .thenReturn(testEventFullDto);
        mvc.perform(put("/admin/events/1")
                        .content(mapper.writeValueAsString(testAdminUpdateEventRequest))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void publishEvent() throws Exception {
        when(eventService.publishEvent(anyLong())).thenReturn(testEventFullDto);
        mvc.perform(patch("/admin/events/1/publish"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void rejectEvent() throws Exception {
        when(eventService.rejectEvent(anyLong())).thenReturn(testEventFullDto);
        mvc.perform(patch("/admin/events/1/reject"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }
}