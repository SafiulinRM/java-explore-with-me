package exploreWithMe.controller.adminAPI;

import exploreWithMe.dto.event.AdminUpdateEventRequest;
import exploreWithMe.dto.event.EventFullDto;
import exploreWithMe.filter.EventAdminFilter;
import exploreWithMe.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @Positive @RequestParam(defaultValue = "10") Integer size,
            EventAdminFilter filter) {
        var events = eventService.getEvents(filter);
        log.info("События найдены");
        return events;
    }

    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest updateEventRequest) {
        var event = eventService.putEvent(eventId, updateEventRequest);
        log.info("Событие отредактировано");
        return event;
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        var event = eventService.publishEvent(eventId);
        log.info("Событие опубликовано");
        return event;
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        var event = eventService.rejectEvent(eventId);
        log.info("Событие отклонено");
        return event;
    }
}
