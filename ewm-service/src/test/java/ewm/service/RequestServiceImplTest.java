package ewm.service;

import ewm.dto.ParticipationRequestDto;
import ewm.exception.RequestException;
import ewm.exception.StateEventException;
import ewm.exception.UserEventException;
import ewm.model.*;
import ewm.repo.EventRepository;
import ewm.repo.RequestRepository;
import ewm.repo.UserRepository;
import ewm.util.status.EventState;
import ewm.util.status.RequestStatus;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class RequestServiceImplTest {
    @Mock
    RequestRepository requestRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    RequestServiceImpl requestService;
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String EXCEPTION_MESSAGE = "Ошибка исключения";
    private final Category testCategory = new Category(1L, "category");
    private final User testUser = new User("user1@mail.com", 1L, "user");
    private final User testRequester = new User("requester1@mail.com", 2L, "requester");
    private final Comment testComment = new Comment("comment", testUser);
    private final Location testLocation =
            new Location(1L, (float) 64.6, (float) 64.67);
    private final Event testEvent = new Event(
            List.of(testComment),
            "annotation",
            testCategory,
            11L,
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
    void getRequestsOfEventIfUserNotInitiator() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(requestRepository.findByEventId(anyLong())).thenReturn(List.of(testRequest));
        val exception = assertThrows(UserEventException.class,
                () -> requestService.getRequestsOfEvent(2L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "User 2 not initiator of event 1");
    }

    @Test
    void getRequestsOfEventIfUserIsInitiator() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(requestRepository.findByEventId(anyLong())).thenReturn(List.of(testRequest));
        val requests = requestService.getRequestsOfEvent(1L, 1L);
        assertEquals(requests.get(0), testRequestDto, "Запросы к событию не получены");
    }

    @Test
    void confirmRequestIfRequestModerationIsFalse() {
        Event event = testEvent;
        event.setRequestModeration(false);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val exception = assertThrows(StateEventException.class,
                () -> requestService.confirmRequest(1L, 1L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "Лимит заявок равен 0 или отключена пре-модерация заявок");
    }

    @Test
    void confirmRequestIfConfirmedRequestsIsOver() {
        Event event = testEvent;
        event.setConfirmedRequests(12L);
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val exception = assertThrows(RequestException.class,
                () -> requestService.confirmRequest(1L, 1L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "У события достигнут лимит запросов на участие");
    }

    @Test
    void confirmRequest() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val request = requestService.confirmRequest(1L, 1L, 1L);
        assertEquals(request.getStatus(), RequestStatus.CONFIRMED, "Запрос не подтвердился");
    }

    @Test
    void rejectRequest() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val request = requestService.rejectRequest(1L, 1L, 1L);
        assertEquals(request.getStatus(), RequestStatus.REJECTED, "Запрос не отклонился");
    }

    @Test
    void getRequestsOfUser() {
        when(requestRepository.findByRequesterId(anyLong())).thenReturn(List.of(testRequest));
        val requests = requestService.getRequestsOfUser(1L);
        assertEquals(requests.get(0), testRequestDto, "Запросы пользователя не получены");
    }

    @Test
    void addRequest() {
        Event event = testEvent;
        event.setRequestModeration(false);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testRequester));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Request request = testRequest;
        request.setStatus(RequestStatus.PENDING);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        val savedRequest = requestService.addRequest(2L, 1L);
        assertEquals(savedRequest, testRequestDto, "Запрос не сохранился и опубликовался");
    }

    @Test
    void addRequestIfRequestAlreadyExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testRequester));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        Request request = testRequest;
        request.setStatus(RequestStatus.PENDING);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        when(requestRepository.findByRequesterIdAndEventId(anyLong(), anyLong())).thenReturn(request);
        val exception = assertThrows(RequestException.class,
                () -> requestService.addRequest(2L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "Такой запрос уже существует");
    }

    @Test
    void addRequestIfRequesterIsInitiator() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(testEvent));
        Request request = testRequest;
        request.setStatus(RequestStatus.PENDING);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        val exception = assertThrows(RequestException.class,
                () -> requestService.addRequest(1L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "Инициатор события не может добавить запрос на участие в своём событии");
    }

    @Test
    void addRequestIfEventStateIsNotPublished() {
        Event event = testEvent;
        event.setState(EventState.PENDING);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testRequester));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Request request = testRequest;
        request.setStatus(RequestStatus.PENDING);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        val exception = assertThrows(RequestException.class,
                () -> requestService.addRequest(2L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "Нельзя участвовать в неопубликованном событии");
    }

    @Test
    void addRequestIfConfirmedRequestsIsOver() {
        Event event = testEvent;
        event.setConfirmedRequests(12L);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testRequester));
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Request request = testRequest;
        request.setStatus(RequestStatus.PENDING);
        when(requestRepository.save(any(Request.class))).thenReturn(request);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));
        val exception = assertThrows(RequestException.class,
                () -> requestService.addRequest(2L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "У события достигнут лимит запросов на участие");
    }

    @Test
    void cancelRequest() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val request = requestService.cancelRequest(2L, 1L);
        assertEquals(request.getStatus(), RequestStatus.CANCELED, "Запрос не отменился");
    }

    @Test
    void cancelRequestIfUserIsNotRequester() {
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(testRequest));
        val exception = assertThrows(RequestException.class,
                () -> requestService.cancelRequest(1L, 1L), EXCEPTION_MESSAGE);
        assertEquals(exception.getMessage(), "User 1 not initiator of request 1");
    }
}