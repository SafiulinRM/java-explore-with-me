package ewm.api.privateApiController.adminController;

import ewm.dto.event.AdminUpdateEventRequest;
import ewm.dto.event.EventFullDto;
import ewm.service.interfaces.EventService;
import ewm.util.filter.EventAdminFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * Закрытый контроллер событий ({@link ewm.model.Event})
 * для администратора
 *
 * @author safiulinrm
 * @see ewm.api.publicApi.publicController.EventController
 * @see ewm.api.privateApiController.userPrivateController.UserEventsController
 */
@Slf4j
@Validated
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {
    /**
     * Сервис для работы с событиями {@link EventService}
     */
    private final EventService eventService;

    /**
     * Эндпоинт возвращает полную информацию обо всех событиях подходящих
     * под переданные условия
     *
     * @param users      список id пользователей, чьи события нужно найти
     * @param states     список состояний в которых находятся искомые события
     * @param categories список id категорий в которых будет вестись поиск
     * @param rangeStart дата и время не позже которых должно произойти событие
     * @param rangeEnd   дата и время не позже которых должно произойти событие
     * @param from       количество событий, которые нужно пропустить для формирования
     *                   текущего набора Default value : 0
     * @param size       количество событий в наборе Default value : 10
     * @param filter     параметры упаковываются в класс фильтр {@link EventAdminFilter}
     * @return возвращает список событий {@link List} {@link EventFullDto}}
     */
    @GetMapping
    public List<EventFullDto> getEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
            @Positive @RequestParam(required = false, defaultValue = "10") Integer size,
            EventAdminFilter filter) {
        var events = eventService.getEvents(filter);
        log.info("События найдены");
        return events;
    }

    /**
     * Редактирование данных любого события администратором. Валидация данных не требуется.
     *
     * @param eventId            id события
     * @param updateEventRequest Данные для изменения информации о событии {@link AdminUpdateEventRequest}
     * @return Возвращает полную информацию об обновленном событии {@link EventFullDto}
     */
    @PutMapping("/{eventId}")
    public EventFullDto putEvent(@PathVariable Long eventId,
                                 @RequestBody AdminUpdateEventRequest updateEventRequest) {
        var event = eventService.putEvent(eventId, updateEventRequest);
        log.info("Событие отредактировано");
        return event;
    }

    /**
     * Публикация события.
     * Обратите внимание:
     * дата начала события должна быть не ранее чем за час от даты публикации
     * событие должно быть в состоянии ожидания публикации
     *
     * @param eventId id события
     * @return возвращает опубликованное событие {@link EventFullDto}
     */
    @PatchMapping("/{eventId}/publish")
    public EventFullDto publishEvent(@PathVariable Long eventId) {
        var event = eventService.publishEvent(eventId);
        log.info("Событие опубликовано");
        return event;
    }

    /**
     * Отклонение событий.
     * Обратите внимание: событие не должно быть опубликовано.
     *
     * @param eventId id события
     * @return возвращает отклоненное событие {@link EventFullDto}
     */
    @PatchMapping("/{eventId}/reject")
    public EventFullDto rejectEvent(@PathVariable Long eventId) {
        var event = eventService.rejectEvent(eventId);
        log.info("Событие отклонено");
        return event;
    }
}
