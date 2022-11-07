package ewm.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import ewm.service.interfaces.EventService;
import ewm.util.client.EventClient;
import ewm.dto.LocationShort;
import ewm.dto.event.*;
import ewm.dto.stats.EndpointHit;
import ewm.exception.EventDateException;
import ewm.exception.NotFoundException;
import ewm.exception.StateEventException;
import ewm.exception.UserEventException;
import ewm.util.filter.EventAdminFilter;
import ewm.util.filter.EventUserFilter;
import ewm.model.*;
import ewm.repo.CategoryRepository;
import ewm.repo.EventRepository;
import ewm.repo.LocationRepository;
import ewm.repo.UserRepository;
import ewm.util.status.EventState;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ewm.mapper.EventMapper.*;

/**
 * Реализация интерфейса по работе с Событиями {@link EventService}
 *
 * @author safiulinrm
 * @see Event
 */
@Slf4j
@Service
@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    /**
     * Сортировка по умолчанию по полю id класса категории
     */
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    /**
     * Общий формат времени во всей программе
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    /**
     * Сообщение для исключения об отсутствии события в базе данных
     */
    private static final String EVENT_NOT_FOUND = "Event not found ";
    /**
     * Сообщение для исключения об отсутствии пользователя в базе данных
     */
    private static final String USER_NOT_FOUND = "User not found ";
    /**
     * Сообщение для исключения об отсутствии категории в базе данных
     */
    private static final String CATEGORY_NOT_FOUND = "Category not found ";
    /**
     * Сообщение для исключения о не правильной дате и времени в событии
     */
    private static final String EVENT_DATE_MESSAGE = "Дата и время на которые намечено событие не может " +
            "быть раньше, чем через два часа от текущего момента ";
    /**
     * Сообщение для исключения о не правильном статусе в событии
     */
    private static final String STATE_MESSAGE = "Событие должно быть в состоянии ожидания публикациию. " +
            "Текущий статус ";
    /**
     * Репозиторий для работы с событиями {@link EventRepository}
     */
    private final EventRepository eventRepository;
    /**
     * Репозиторий для работы с категориями событий {@link CategoryRepository}
     */
    private final CategoryRepository categoryRepository;
    /**
     * Репозиторий для работы с пользователями событий {@link UserRepository}
     */
    private final UserRepository userRepository;
    /**
     * Репозиторий для работы с локациями событий {@link LocationRepository}
     */
    private final LocationRepository locationRepository;
    /**
     * клиент для отправления данных о получении события и запроса статистики
     * серверу статистики {@link EventClient}
     */
    private final EventClient eventClient;

    @Override
    @Transactional
    public List<EventShortDto> getEvents(EventUserFilter filter, @NonNull HttpServletRequest request) {
        setViewsOfEvents(filter, request);
        Iterable<Event> events = eventRepository.findAll(formatExpression(filter), makePageable(filter));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsFullDto(result);
    }

    @Override
    @Transactional
    public EventFullDto getEvent(Long id, @NonNull HttpServletRequest request) {
        Event event = setViewOfEvent(id, request);
        return toEventFullDto(event);
    }

    @Override
    public List<EventShortDto> getEventsOfUser(Long userId, int from, int size) {
        Page<Event> events = eventRepository.findByInitiatorId(userId, makePageable(from, size));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsShortDto(result);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(updateEvent.getEventId())
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + updateEvent.getEventId()));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        if (event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING)) {
            toUserUpdateEvent(updateEvent, event);
        } else {
            throw new StateEventException("Изменить можно только отмененные события или события в состоянии ожидания модерации."
                    + " Текущий статус: " + event.getState());
        }
        return toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND + userId));
        Location location = getLocation(newEventDto.getLocation());
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + newEventDto.getCategory()));
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);
        Event event = eventRepository.save(toEvent(newEventDto, initiator, location, category, eventDate));
        return toEventFullDto(event);
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        return toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        if (!event.getState().equals(EventState.PENDING)) {
            throw new StateEventException(STATE_MESSAGE + event.getState());
        }
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    @Override
    public List<EventFullDto> getEvents(@NonNull EventAdminFilter filter) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(filter), makePageable(filter));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsFullDto(result);
    }

    @Override
    @Transactional
    public EventFullDto putEvent(Long eventId, @NonNull AdminUpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        toAdminUpdateEvent(updateEvent, event);
        return toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new EventDateException("Дата и время на которые намечено событие не может быть раньше," +
                    " чем через 1 час от текущего момента " + event.getEventDate());
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new StateEventException(STATE_MESSAGE + event.getState());
        }
        event.setState(EventState.PUBLISHED);
        return toEventFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + eventId));
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new StateEventException("Событие не должно быть опубликовано. " +
                    "Текущий статус " + event.getState());
        }
        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    /**
     * Настройка пагинации
     *
     * @param filter пользовательский фильтр получения событий {@link EventUserFilter}
     * @return {@link Pageable}
     */
    private Pageable makePageable(@NonNull EventUserFilter filter) {
        if (filter.getSort().equals("EVENT_DATE")) {
            filter.setSort("eventDate");
        } else if (filter.getSort().equals("VIEWS")) {
            filter.setSort("views");
        } else {
            filter.setSort("id");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, filter.getSort());
        int page = filter.getFrom() / filter.getSize();
        return PageRequest.of(page, filter.getSize(), sort);
    }

    /**
     * Настройка пагинации
     *
     * @param filter фильтр получения событий администратором{@link EventAdminFilter}
     * @return {@link Pageable}
     */
    private Pageable makePageable(@NonNull EventAdminFilter filter) {
        int page = filter.getFrom() / filter.getSize();
        return PageRequest.of(page, filter.getSize(), DEFAULT_SORT);
    }

    /**
     * Настройка пагинации
     *
     * @param from количество элементов, которые нужно пропустить для формирования текущего набора
     *             Default value : 0
     * @param size количество элементов в наборе
     *             Default value : 10
     * @return {@link Pageable}
     */
    private Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, DEFAULT_SORT);
    }

    /**
     * Настройка фильтра необязательных параметров для получения событий в репозитории
     *
     * @param filter пользовательский фильтр получения событий {@link EventUserFilter}
     * @return {@link BooleanExpression}
     */
    private BooleanExpression formatExpression(@NonNull EventUserFilter filter) {
        BooleanExpression result;
        result = QEvent.event.paid.eq(filter.getPaid());
        if (filter.getText() != null && !Objects.equals(filter.getText(), "0")) {
            result.and(QEvent.event.description.like(filter.getText()));
        }
        if (filter.getCategories() != null && filter.getCategories().get(0) != 0) {
            result = result.and(QEvent.event.category.id.in(filter.getCategories()));
        }
        if (filter.getRangeStart() != null) {
            result = result.and(QEvent.event.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            result = result.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
        }
        if (filter.getOnlyAvailable().equals(true)) {
            result = result.and(QEvent.event.confirmedRequests.gt(QEvent.event.participantLimit));
        }
        return result;
    }

    /**
     * Настройка фильтра необязательных параметров для получения событий в репозитории
     *
     * @param filter фильтр получения событий администратором{@link EventAdminFilter}
     * @return {@link BooleanExpression}
     */
    private BooleanExpression formatExpression(@NonNull EventAdminFilter filter) {
        BooleanExpression result = QEvent.event.initiator.id.in(filter.getUsers());
        if (filter.getCategories() != null) {
            result = result.and(QEvent.event.category.id.in(filter.getCategories()));
        }
        if (filter.getRangeStart() != null) {
            result = result.and(QEvent.event.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            result = result.and(QEvent.event.eventDate.before(filter.getRangeEnd()));
        }
        return result;
    }

    /**
     * Маппирование обновленной информации события
     *
     * @param updateEvent обновленная информация инициатором {@link UpdateEventRequest}
     * @param event       cобытие которое хотят обновить {@link Event}
     */
    private void toUserUpdateEvent(UpdateEventRequest updateEvent, Event event) {
        event.setState(EventState.PENDING);
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER);
            if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
                throw new EventDateException(EVENT_DATE_MESSAGE + eventDate);
            }
            event.setEventDate(eventDate);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }

    /**
     * Маппирование обновленной информации события
     *
     * @param updateEvent обновленная информация администратором {@link AdminUpdateEventRequest}
     * @param event       cобытие которое хотят обновить {@link Event}
     */
    private void toAdminUpdateEvent(@NonNull AdminUpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException(CATEGORY_NOT_FOUND + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (!updateEvent.getEventDate().isBlank()) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER);
            if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
                throw new EventDateException(EVENT_DATE_MESSAGE + eventDate);
            }
            event.setEventDate(eventDate);
        }
        if (updateEvent.getLocation() != null) {
            event.setLocation(getLocation(updateEvent.getLocation()));
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            event.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
    }

    /**
     * Получить локацию по аналогичным данным дто
     *
     * @param locationShort {@link LocationShort}
     * @return {@link Location}
     */
    private Location getLocation(@Validated LocationShort locationShort) {
        Location location = locationRepository.findByLatAndLon(locationShort.getLat(), locationShort.getLon());
        if (location == null) {
            location = locationRepository.save(new Location(locationShort));
        }
        return location;
    }

    /**
     * Проверка инициатора события
     *
     * @param userId      id возможного инициатора
     * @param initiatorId id события
     */
    private void checkInitiatorOfEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new UserEventException("User " + userId + " not initiator of event " + initiatorId);
        }
    }

    /**
     * Получить статистику через Клиента событий {@link EventClient}
     *
     * @param request информация о запросе событий
     * @return количество просмотров события
     */
    private Long getStats(@NonNull HttpServletRequest request) {
        var stats = eventClient.getStats(List.of(request.getRequestURI()));
        return stats.getBody()[0].getHits();
    }

    /**
     * Отправить информацию о запросе событий через Клиента событий {@link EventClient}
     *
     * @param request информация о запросе событий
     */
    private void postHit(@NonNull HttpServletRequest request) {
        EndpointHit endpointHit = new EndpointHit("ewm-service",
                request.getRequestURI(),
                request.getRemoteAddr());
        eventClient.postHit(endpointHit);
    }

    /**
     * Определение просмотров каждого события в списке
     *
     * @param filter  пользовательский фильтр поиска событий{@link EventUserFilter}
     * @param request сведения о запросе событий {@link HttpServletRequest}
     */
    protected void setViewsOfEvents(@NonNull EventUserFilter filter, @NonNull HttpServletRequest request) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(filter), makePageable(filter));
        postHit(request);
        for (Event event : events) {
            try {
                Long views = eventClient.getStats(List.of("/events/" + event.getId()))
                        .getBody()[0].getHits();
                event.setViews(views);
            } catch (ArrayIndexOutOfBoundsException e) {
                event.setViews(0);
            }
        }
    }

    protected Event setViewOfEvent(Long id, HttpServletRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND + id));
        postHit(request);
        event.setViews(getStats(request));
        return event;
    }
}
