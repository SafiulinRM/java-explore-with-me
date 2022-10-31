package exploreWithMe.mapper;

import exploreWithMe.dto.event.EventFullDto;
import exploreWithMe.dto.event.EventShortDto;
import exploreWithMe.dto.event.NewEventDto;
import exploreWithMe.model.Category;
import exploreWithMe.model.Event;
import exploreWithMe.model.Location;
import exploreWithMe.model.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static exploreWithMe.mapper.CategoryMapper.toCategoryDto;
import static exploreWithMe.mapper.UserMapper.toUserShortDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {
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

    public static List<EventShortDto> toEventsShortDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    public static List<EventFullDto> toEventsFullDto(List<Event> events) {
        return events.stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }

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
