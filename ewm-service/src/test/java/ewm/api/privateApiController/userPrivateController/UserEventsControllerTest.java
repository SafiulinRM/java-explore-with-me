package ewm.api.privateApiController.userPrivateController;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.LocationShort;
import ewm.dto.ParticipationRequestDto;
import ewm.dto.category.CategoryDto;
import ewm.dto.comment.CommentDto;
import ewm.dto.event.EventFullDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.event.NewEventDto;
import ewm.dto.event.UpdateEventRequest;
import ewm.dto.user.UserShortDto;
import ewm.model.*;
import ewm.service.interfaces.EventService;
import ewm.service.interfaces.RequestService;
import ewm.util.status.EventState;
import ewm.util.status.RequestStatus;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserEventsController.class)
class UserEventsControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private EventService eventService;
    @MockBean
    private RequestService requestService;
    @Autowired
    private MockMvc mvc;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final CategoryDto testCategoryDto = new CategoryDto(1L, "category");
    private final Category testCategory = new Category(1L, "category");
    private final UserShortDto testShortUserDto = new UserShortDto(1L, "user");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final UserShortDto testUserShortDto = new UserShortDto(1L, "user");

    private final Comment testComment = new Comment("comment", testUser);
    private final CommentDto testCommentDto = new CommentDto("comment",
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
    private final EventShortDto testEventShortDto = new EventShortDto(
            "annotation",
            testCategoryDto,
            10L,
            testEvent.getEventDate(),
            1L,
            testShortUserDto,
            true,
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

    private final UpdateEventRequest testUpdateEvent = new UpdateEventRequest(
            "annotation",
            1L,
            "description",
            testEvent.getEventDate().format(FORMATTER),
            1L,
            true,
            12L,
            "title");
    private final NewEventDto testNewEventDto = new NewEventDto(
            "annotation",
            1L,
            "description",
            testEvent.getEventDate().format(FORMATTER),
            testLocationDto,
            true,
            12L,
            true,
            "title");
    private final User testRequester = new User("requester1@mail.com", 2L, "requester");
    private final Request testRequest = new Request(
            testEvent,
            1L,
            testRequester,
            RequestStatus.CONFIRMED);
    private final ParticipationRequestDto testRequestDto = new ParticipationRequestDto(
            testRequest.getCreated().format(FORMATTER),
            1L,
            1L,
            2L,
            RequestStatus.CONFIRMED);

    @Test
    void getEventsOfUser() throws Exception {
        when(eventService.getEventsOfUser(anyLong(), anyInt(), anyInt())).thenReturn(List.of(testEventShortDto));
        mvc.perform(get("/users/1/events"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testEventShortDto))));
    }

    @Test
    void updateEvent() throws Exception {
        when(eventService.updateEvent(anyLong(), any(UpdateEventRequest.class))).thenReturn(testEventFullDto);
        mvc.perform(patch("/users/1/events")
                        .content(mapper.writeValueAsString(testUpdateEvent))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void addEvent() throws Exception {
        when(eventService.addEvent(anyLong(), any(NewEventDto.class))).thenReturn(testEventFullDto);
        mvc.perform(post("/users/1/events")
                        .content(mapper.writeValueAsString(testNewEventDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void getEvent() throws Exception {
        when(eventService.getEvent(anyLong(), anyLong())).thenReturn(testEventFullDto);
        mvc.perform(get("/users/1/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void cancelEvent() throws Exception {
        when(eventService.cancelEvent(anyLong(), anyLong())).thenReturn(testEventFullDto);
        mvc.perform(patch("/users/1/events/1"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testEventFullDto)));
    }

    @Test
    void getRequestsOfEvent() throws Exception {
        when(requestService.getRequestsOfEvent(anyLong(), anyLong())).thenReturn(List.of(testRequestDto));
        mvc.perform(get("/users/1/events/1/requests"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(List.of(testRequestDto))));
    }

    @Test
    void confirmRequest() throws Exception {
        when(requestService.confirmRequest(anyLong(), anyLong(), anyLong())).thenReturn(testRequestDto);
        mvc.perform(patch("/users/1/events/1/requests/1/confirm"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testRequestDto)));
    }

    @Test
    void rejectRequest() throws Exception {
        when(requestService.rejectRequest(anyLong(), anyLong(), anyLong())).thenReturn(testRequestDto);
        mvc.perform(patch("/users/1/events/1/requests/1/reject"))
                .andExpect(status().isOk())
                .andExpect(content().json(mapper.writeValueAsString(testRequestDto)));
    }
}