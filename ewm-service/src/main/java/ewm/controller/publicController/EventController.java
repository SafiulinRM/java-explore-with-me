package ewm.controller.publicController;

import ewm.dto.event.EventFullDto;
import ewm.dto.event.EventShortDto;
import ewm.filter.EventUserFilter;
import ewm.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @RequestParam(required = false) String rangeStart,
                                         @RequestParam(required = false) String rangeEnd,
                                         @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false, defaultValue = "0") int from,
                                         @RequestParam(required = false, defaultValue = "10") int size,
                                         EventUserFilter filter,
                                         HttpServletRequest request) {
        var events = eventService.getEvents(filter, request);
        log.info("События найдены");
        return events;
    }

    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.getEvent(id, request);
        log.info("Событие найдено");
        return eventFullDto;
    }
}
