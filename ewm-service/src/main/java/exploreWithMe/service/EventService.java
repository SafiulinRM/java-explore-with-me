package exploreWithMe.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import exploreWithMe.dto.LocationShort;
import exploreWithMe.dto.event.*;
import exploreWithMe.exception.EventDateException;
import exploreWithMe.exception.NotFoundException;
import exploreWithMe.exception.StateException;
import exploreWithMe.exception.UserEventException;
import exploreWithMe.filter.EventAdminFilter;
import exploreWithMe.filter.EventUserFilter;
import exploreWithMe.model.*;
import exploreWithMe.repo.CategoryRepository;
import exploreWithMe.repo.EventRepository;
import exploreWithMe.repo.LocationRepository;
import exploreWithMe.repo.UserRepository;
import exploreWithMe.status.EventState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static exploreWithMe.mapper.EventMapper.*;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventService {
    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.ASC, "id");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    public List<EventShortDto> getEvents(EventUserFilter filter) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(filter), makePageable(filter));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsFullDto(result);
    }

    public EventFullDto getEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event not found " + id));
        return toEventFullDto(event);
    }

    public List<EventShortDto> getEventsOfUser(Long userId, int from, int size) {
        Page<Event> events = eventRepository.findByInitiatorId(userId, makePageable(from, size));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsShortDto(result);
    }

    @Transactional
    public EventFullDto updateEvent(Long userId, UpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(updateEvent.getEventId())
                .orElseThrow(() -> new NotFoundException("Event not found " + updateEvent.getEventId()));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        if (event.getState().equals(EventState.CANCELED) || event.getState().equals(EventState.PENDING)) {
            toUserUpdateEvent(updateEvent, event);
        } else {
            throw new StateException("Изменить можно только отмененные события или события в состоянии ожидания модерации."
                    + " Текущий статус: " + event.getState());
        }
        return toEventFullDto(event);
    }

    @Transactional
    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found " + userId));
        Location location = getLocation(newEventDto.getLocation());
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category not found " + newEventDto.getCategory()));
        LocalDateTime eventDate = LocalDateTime.parse(newEventDto.getEventDate(), FORMATTER);
        Event event = eventRepository.save(toEvent(newEventDto, initiator, location, category, eventDate));
        return toEventFullDto(event);
    }

    public EventFullDto getEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        return toEventFullDto(event);
    }

    @Transactional
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        if (!event.getState().equals(EventState.PENDING)) {
            throw new StateException("Oтменить можно только события в состоянии ожидания модерации."
                    + " Текущий статус: " + event.getState());
        }
        checkInitiatorOfEvent(userId, event.getInitiator().getId());
        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    public List<EventFullDto> getEvents(EventAdminFilter filter) {
        Iterable<Event> events = eventRepository.findAll(formatExpression(filter), makePageable(filter));
        List result = new ArrayList();
        events.forEach(result::add);
        return toEventsFullDto(result);
    }

    @Transactional
    public EventFullDto putEvent(Long eventId, AdminUpdateEventRequest updateEvent) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        toAdminUpdateEvent(updateEvent, event);
        return toEventFullDto(event);
    }

    @Transactional
    public EventFullDto publishEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        if (LocalDateTime.now().plusHours(1).isAfter(event.getEventDate())) {
            throw new EventDateException("Дата и время на которые намечено событие не может быть раньше," +
                    " чем через 1 час от текущего момента " + event.getEventDate());
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new StateException("Событие должно быть в состоянии ожидания публикациию. " +
                    "Текущий статус " + event.getState());
        }
        event.setState(EventState.PUBLISHED);
        return toEventFullDto(event);
    }

    @Transactional
    public EventFullDto rejectEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event not found " + eventId));
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new StateException("Событие не должно быть опубликовано. " +
                    "Текущий статус " + event.getState());
        }
        event.setState(EventState.CANCELED);
        return toEventFullDto(event);
    }

    private Pageable makePageable(EventUserFilter filter) {
        if (filter.getSort().equals("EVENT_DATE")) {
            filter.setSort("eventDate");
        } else if (filter.getSort().equals("VIEWS")) {
            filter.setSort("views");
        }
        Sort sort = Sort.by(Sort.Direction.ASC, filter.getSort());
        int page = filter.getFrom() / filter.getSize();
        return PageRequest.of(page, filter.getSize(), sort);
    }

    private Pageable makePageable(EventAdminFilter filter) {
        int page = filter.getFrom() / filter.getSize();
        return PageRequest.of(page, filter.getSize(), DEFAULT_SORT);
    }

    private Pageable makePageable(int from, int size) {
        int page = from / size;
        return PageRequest.of(page, size, DEFAULT_SORT);
    }

    private BooleanExpression formatExpression(EventUserFilter filter) {
        BooleanExpression result;
        result = QEvent.event.paid.eq(filter.getPaid());
        if (filter.getText() != null && !Objects.equals(filter.getText(), "0")){
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

    private BooleanExpression formatExpression(EventAdminFilter filter) {
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

    private void toUserUpdateEvent(UpdateEventRequest updateEvent, Event event) {
        event.setState(EventState.PENDING);
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found " + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER);
            if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
                throw new EventDateException("Дата и время на которые намечено событие не может быть раньше," +
                        " чем через два часа от текущего момента " + eventDate);
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

    private void toAdminUpdateEvent(AdminUpdateEventRequest updateEvent, Event event) {
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryRepository.findById(updateEvent.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category not found " + updateEvent.getCategory()));
            event.setCategory(category);
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (!updateEvent.getEventDate().isBlank()) {
            LocalDateTime eventDate = LocalDateTime.parse(updateEvent.getEventDate(), FORMATTER);
            if (LocalDateTime.now().plusHours(2).isAfter(eventDate)) {
                throw new EventDateException("Дата и время на которые намечено событие не может быть раньше," +
                        " чем через два часа от текущего момента " + eventDate);
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

    private Location getLocation(LocationShort locationShort) {
        Location location = locationRepository.findByLatAndLon(locationShort.getLat(), locationShort.getLon());
        if (location == null) {
            location = locationRepository.save(new Location(locationShort));
        }
        return location;
    }

    private void checkInitiatorOfEvent(long userId, long initiatorId) {
        if (initiatorId != userId) {
            throw new UserEventException("User " + userId + " not initiator of event " + initiatorId);
        }
    }
}
