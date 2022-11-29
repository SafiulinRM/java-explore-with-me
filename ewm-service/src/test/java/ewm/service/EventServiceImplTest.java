package ewm.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import ewm.dto.LocationShort;
import ewm.dto.category.CategoryDto;
import ewm.dto.comment.CommentDto;
import ewm.dto.event.*;
import ewm.dto.stats.ViewStats;
import ewm.dto.user.UserShortDto;
import ewm.exception.EventDateException;
import ewm.exception.StateEventException;
import ewm.exception.UserEventException;
import ewm.model.*;
import ewm.repo.CategoryRepository;
import ewm.repo.EventRepository;
import ewm.repo.LocationRepository;
import ewm.repo.UserRepository;
import ewm.util.client.EventClient;
import ewm.util.filter.EventAdminFilter;
import ewm.util.filter.EventUserFilter;
import ewm.util.status.EventState;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EventServiceImplTest {
    @Mock
    EventRepository eventRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    LocationRepository locationRepository;
    @Mock
    EventClient eventClient;
    @InjectMocks
    EventServiceImpl eventService;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final ViewStats[] testStats = new ViewStats[]{
            new ViewStats("app", "event/1", 1L)};
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
    private final EventUserFilter testEventUserFilter = new EventUserFilter(
            "annotation",
            List.of(1L),
            true,
            LocalDateTime.now(),
            LocalDateTime.now().plusHours(10),
            true,
            "VIEWS",
            0,
            10);
    private final EventAdminFilter testEventAdminFilter = new EventAdminFilter(
            List.of(1L),
            List.of("PUBLISHED"),
            List.of(1L),
            null,
            LocalDateTime.now().plusDays(1),
            0,
            10
    );
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
    private final HttpServletRequest request = new MockHttpServletRequest("Get", "event/1");

    @Test
    void userGetEvents() {
        when(eventRepository.findAll(any(BooleanExpression.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testEvent)));
        when(eventClient.getStats(any())).thenReturn(ResponseEntity.of(Optional.of(testStats)));
        List<EventShortDto> eventsShortDto = eventService.getEvents(testEventUserFilter, request);
        assertEquals(eventsShortDto.get(0), testEventShortDto,
                "События по фильтру пользователя пользователя не получены");
    }

    @Test
    void getEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(eventClient.getStats(any())).thenReturn(ResponseEntity.of(Optional.of(testStats)));
        val eventDto = eventService.getEvent(1L, request);
        assertEquals(eventDto, testEventFullDto, "Событие не получено");
    }

    @Test
    void getEventsOfUser() {
        when(eventRepository.findByInitiatorId(anyLong(), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testEvent)));
        val events = eventService.getEventsOfUser(1L, 0, 10);
        assertEquals(events.get(0), testEventShortDto);
    }

    @Test
    void updateEventIfStateIsPending() {
        Event event = testEvent;
        event.setState(EventState.PENDING);
        EventFullDto eventFullDto = testEventFullDto;
        eventFullDto.setState("PENDING");
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        val updateEvent = eventService.updateEvent(1L, testUpdateEvent);
        assertEquals(updateEvent, eventFullDto, "Событие не обновилось");
    }

    @Test
    void updateEventIfStateIsPublished() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        val exception = assertThrows(StateEventException.class,
                () -> eventService.updateEvent(1L, testUpdateEvent), "Ошибка исключения");
        assertEquals(exception.getMessage(), "Событие не должно быть опубликовано. " +
                "Текущий статус PUBLISHED");
    }

    @Test
    void updateEventIfUserNotInitiator() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        val exception = assertThrows(UserEventException.class,
                () -> eventService.updateEvent(2L, testUpdateEvent), "Ошибка исключения");
        assertEquals(exception.getMessage(), "User 2 not initiator of event 1");
    }

    @Test
    void addEvent() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);
        when(locationRepository.findByLatAndLon(anyFloat(), anyFloat())).thenReturn(testLocation);
        val event = eventService.addEvent(1L, testNewEventDto);
        assertEquals(event, testEventFullDto, "Событие не создалось");
    }

    @Test
    void initiatorGetEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        val event = eventService.getEvent(1L, 1L);
        assertEquals(event, testEventFullDto, "Событие не получено инициатором");
    }

    @Test
    void cancelEventIfStateIsPublished() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        val exception = assertThrows(StateEventException.class,
                () -> eventService.cancelEvent(1L, 1L));
        assertEquals(exception.getMessage(), "Событие должно быть в состоянии ожидания публикации. " +
                "Текущий статус PUBLISHED");
    }

    @Test
    void cancelEventIfStateIsPending() {
        Event event = testEvent;
        event.setState(EventState.PENDING);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        val canceledEvent = eventService.cancelEvent(1L, 1L);
        assertEquals(canceledEvent.getState(), "CANCELED", "Событие не отменено");
    }

    @Test
    void testGetEvents() {
        when(eventRepository.findAll(any(BooleanExpression.class), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(List.of(testEvent)));
        val events = eventService.getEvents(testEventAdminFilter);
        assertEquals(events.get(0), testEventFullDto,
                "События по фильтру администратора не получены");
    }

    @Test
    void putEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(testCategory));
        when(locationRepository.save(any(Location.class))).thenReturn(testLocation);
        val event = eventService.putEvent(1L, testAdminUpdateEventRequest);
        assertEquals(event, testEventFullDto, "Событие не обновилось администратором");
    }

    @Test
    void publishEventIfEventDateIsWrong() {
        Event event = testEvent;
        event.setEventDate(LocalDateTime.now().plusMinutes(30));
        event.setState(EventState.PENDING);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        val exception = assertThrows(EventDateException.class,
                () -> eventService.publishEvent(1L));
        assertEquals(exception.getMessage(), "Дата и время на которые намечено событие не может быть раньше," +
                " чем через 1 час от текущего момента " + event.getEventDate());
    }

    @Test
    void publishEvent() {
        Event event = testEvent;
        event.setState(EventState.PENDING);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        val publishedEvent = eventService.publishEvent(1L);
        assertEquals(publishedEvent, testEventFullDto, "Событие не опубликовалось");
    }

    @Test
    void rejectEvent() {
        Event event = testEvent;
        event.setState(EventState.PENDING);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        val rejectedEvent = eventService.rejectEvent(1L);
        assertEquals(rejectedEvent.getState(), "" +
                "REJECTED", "Событие не отклонилось");
    }
}