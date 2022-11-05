package ewm.mapper;

import ewm.dto.event.EventFullDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.event.NewEventDto;
import ewm.model.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ewm.mapper.CategoryMapper.toCategoryDto;
import static ewm.mapper.UserMapper.toUserShortDto;

/**
 * Класс, с помощью которого можно преобразовать событие в dto и наоборот
 *
 * @author safiulinrm
 * @see Event
 * @see EventShortDto
 * @see EventFullDto
 * @see NewEventDto
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {
    /**
     * Преобразование события в shortDto
     *
     * @param event данные события {@link Event}
     * @return {@link EventShortDto}
     */
    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    /**
     * Преобразование списка событий в shortDto
     *
     * @param events список событий {@link List {@link Event}}
     * @return {@link List {@link EventShortDto}}
     */
    public static List<EventShortDto> toEventsShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразование списка событий в fullDto
     *
     * @param events список событий {@link List {@link Event}}
     * @return {@link List {@link EventFullDto}}
     */
    public static List<EventFullDto> toEventsFullDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

    /**
     * Преобразование нового события в класс хранения
     *
     * @param eventDto  данные нового события {@link NewEventDto}
     * @param initiator инициатор события {@link User}
     * @param location  локация события {@link Location}
     * @param category  категория события {@link Category}
     * @param eventDate Дата и время события {@link LocalDateTime}
     * @return {@link Event}
     */
    public static Event toEvent(NewEventDto eventDto,
                                User initiator,
                                Location location,
                                Category category,
                                LocalDateTime eventDate) {
        return new Event(eventDto.getAnnotation(),
                category,
                eventDto.getDescription(),
                eventDate,
                initiator,
                location,
                eventDto.getPaid(),
                eventDto.getParticipantLimit(),
                eventDto.getRequestModeration(),
                eventDto.getTitle());
    }

    /**
     * Преобразует событие в fullDto
     *
     * @param event данные события {@link Event}
     * @return {@link EventFullDto}
     */
    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(
                event.getAnnotation(),
                toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate().toString().replace('T', ' '),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState().toString(),
                event.getTitle(),
                event.getViews()
        );
    }
}
