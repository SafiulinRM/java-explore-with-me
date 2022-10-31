package exploreWithMe.controller.privateAPI;

import exploreWithMe.dto.*;
import exploreWithMe.dto.event.EventFullDto;
import exploreWithMe.dto.event.EventShortDto;
import exploreWithMe.dto.event.NewEventDto;
import exploreWithMe.dto.event.UpdateEventRequest;
import exploreWithMe.service.EventService;
import exploreWithMe.service.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventsController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public List<EventShortDto> getEventsOfUser(@PathVariable Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        var events = eventService.getEventsOfUser(userId, from, size);
        log.info("События найдены");
        return events;
    }

    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody @Validated UpdateEventRequest updateEventRequest) {
        var event = eventService.updateEvent(userId, updateEventRequest);
        log.info("Событие обновлено");
        return event;
    }

    @PostMapping
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Validated NewEventDto newEventDto) {
        var event = eventService.addEvent(userId, newEventDto);
        log.info("Событие добавлено");
        return event;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        var event = eventService.getEvent(userId, eventId);
        log.info("Событие найдено");
        return event;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        var event = eventService.cancelEvent(userId, eventId);
        log.info("Событие обновлено");
        return event;
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsOfEvent(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        var requests = requestService.getRequestsOfEvent(userId, eventId);
        log.info("Найдены запросы на участие");
        return requests;
    }

    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        var request = requestService.confirmRequest(userId, eventId, reqId);
        log.info("Заявка подтверждена");
        return request;
    }

    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        var request = requestService.rejectRequest(userId, eventId, reqId);
        log.info("Заявка отклонена");
        return request;
    }
}
