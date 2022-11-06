package ewm.api.publicApi.publicController;

import ewm.dto.event.EventFullDto;
import ewm.dto.event.EventShortDto;
import ewm.service.interfaces.EventService;
import ewm.util.filter.EventUserFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Открытый контроллер событий ({@link ewm.model.Event})
 * для пользователей
 *
 * @author safiulinrm
 * @see ewm.api.privateApiController.userPrivateController.UserEventsController
 * @see ewm.api.privateApiController.adminController.AdminEventsController
 */
@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventController {
    /**
     * Сервис для работы с событиями {@link EventService}
     */
    private final EventService eventService;

    /**
     * Получение событий с возможностью фильтрации.
     * Обратите внимание:
     * это публичный эндпоинт, соответственно в выдаче должны быть только опубликованные события
     * текстовый поиск (по аннотации и подробному описанию) должен быть без учета регистра букв
     * если в запросе не указан диапазон дат [rangeStart-rangeEnd], то нужно выгружать
     * события, которые произойдут позже текущей даты и времени
     * информация о каждом событии должна включать в себя количество просмотров
     * и количество уже одобренных заявок на участие
     * информацию о том, что по этому эндпоинту был осуществлен и обработан запрос,
     * нужно сохранить в сервисе статистики
     *
     * @param text          текст для поиска в содержимом аннотации и подробном описании события
     * @param categories    список идентификаторов категорий в которых будет вестись поиск
     * @param paid          поиск только платных/бесплатных событий
     * @param rangeStart    дата и время не раньше которых должно произойти событие
     * @param rangeEnd      дата и время не позже которых должно произойти событие
     * @param onlyAvailable только события у которых не исчерпан лимит запросов на участие
     *                      Default value : false
     * @param sort          Вариант сортировки: по дате события или по количеству просмотров
     *                      Available values : EVENT_DATE, VIEWS
     * @param from          количество событий, которые нужно пропустить для формирования текущего набора
     *                      Default value : 0
     * @param size          количество событий в наборе
     *                      Default value : 10
     * @param filter        Класс в котором упаковываются все параметры {@link EventUserFilter}
     * @param request       Класс с информацией о запросе событий {@link HttpServletRequest}
     * @return Возвращает список событий {@link List {@link EventShortDto}}
     */
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

    /**
     * Получение подробной информации об опубликованным событии по его идентификатору
     *
     * @param id      id события
     * @param request Класс с информацией о запросе события {@link HttpServletRequest}
     * @return Возвращает список событий {@link EventFullDto}
     */
    @GetMapping("/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest request) {
        EventFullDto eventFullDto = eventService.getEvent(id, request);
        log.info("Событие найдено");
        return eventFullDto;
    }
}
