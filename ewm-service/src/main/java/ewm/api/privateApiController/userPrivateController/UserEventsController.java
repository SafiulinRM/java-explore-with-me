package ewm.api.privateApiController.userPrivateController;

import ewm.dto.*;
import ewm.dto.event.EventFullDto;
import ewm.dto.event.EventShortDto;
import ewm.dto.event.NewEventDto;
import ewm.dto.event.UpdateEventRequest;
import ewm.service.interfaces.EventService;
import ewm.service.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Закрытый контроллер событий ({@link ewm.model.Event})
 * для пользователей
 *
 * @author safiulinrm
 * @see ewm.api.publicApi.publicController.EventController
 * @see ewm.api.privateApiController.adminController.AdminEventsController
 */
@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventsController {
    /**
     * Сервис для работы с событиями {@link EventService}
     */
    private final EventService eventService;
    /**
     * Сервис для работы с запросами на участие в событии {@link RequestService}
     */
    private final RequestService requestService;

    /**
     * Получение событий, добавленных текущим пользователем
     *
     * @param userId id текущего пользователя
     * @param from   количество элементов, которые нужно пропустить для формирования
     *               текущего набора Default value : 0
     * @param size   количество элементов в наборе
     *               Default value : 10
     * @return Возвращает список событий {@link List {@link EventShortDto}}
     */
    @GetMapping
    public List<EventShortDto> getEventsOfUser(@PathVariable Long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        var events = eventService.getEventsOfUser(userId, from, size);
        log.info("События найдены");
        return events;
    }

    /**
     * Изменение события добавленного текущим пользователем.
     * Обратите внимание:
     * изменить можно только отмененные события или события в состоянии ожидания модерации
     * если редактируется отменённое событие, то оно автоматически переходит в состояние ожидания модерации
     * дата и время на которые намечено событие не может быть раньше, чем через два часа от текущего момента
     *
     * @param userId             id текущего пользователя
     * @param updateEventRequest Новые данные события {@link UpdateEventRequest}
     * @return возвращает обновленное событие {@link EventFullDto}
     */
    @PatchMapping
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @RequestBody @Validated UpdateEventRequest updateEventRequest) {
        var event = eventService.updateEvent(userId, updateEventRequest);
        log.info("Событие обновлено");
        return event;
    }

    /**
     * Добавление нового события.
     * Обратите внимание: дата и время на которые намечено событие не может быть раньше,
     * чем через два часа от текущего момента
     *
     * @param userId      id текущего пользователя
     * @param newEventDto данные добавляемого события {@link NewEventDto}
     * @return возвращает добавленное событие {@link EventFullDto}
     */
    @PostMapping
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody @Validated NewEventDto newEventDto) {
        var event = eventService.addEvent(userId, newEventDto);
        log.info("Событие добавлено");
        return event;
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return возвращает полную информацию о событии {@link EventFullDto}
     */
    @GetMapping("/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        var event = eventService.getEvent(userId, eventId);
        log.info("Событие найдено");
        return event;
    }

    /**
     * Отмена события добавленного текущим пользователем.
     * Обратите внимание: Отменить можно только событие в состоянии ожидания модерации.
     *
     * @param userId  id текущего пользователя
     * @param eventId id отменяемого события
     * @return возвращает отмененное событие {@link EventFullDto}
     */
    @PatchMapping("/{eventId}")
    public EventFullDto cancelEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId) {
        var event = eventService.cancelEvent(userId, eventId);
        log.info("Событие обновлено");
        return event;
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return возвращает список запросов на участие в событии {@link List {@link ParticipationRequestDto}}
     */
    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsOfEvent(@PathVariable Long userId,
                                                            @PathVariable Long eventId) {
        var requests = requestService.getRequestsOfEvent(userId, eventId);
        log.info("Найдены запросы на участие");
        return requests;
    }

    /**
     * Подтверждение чужой заявки на участие в событии текущего пользователя.
     * Обратите внимание:
     * если для события лимит заявок равен 0 или отключена пре-модерация заявок,
     * то подтверждение заявок не требуется
     * нельзя подтвердить заявку, если уже достигнут лимит по заявкам на данное событие
     * если при подтверждении данной заявки, лимит заявок для события исчерпан,
     * то все неподтверждённые заявки необходимо отклонить
     *
     * @param userId  id текущего пользователя
     * @param eventId id события текущего пользователя
     * @param reqId   id заявки, которую подтверждает текущий пользователь
     * @return возвращает подтвержденный запрос на участие в событии {@link ParticipationRequestDto}
     */
    @PatchMapping("/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequest(@PathVariable Long userId,
                                                  @PathVariable Long eventId,
                                                  @PathVariable Long reqId) {
        var request = requestService.confirmRequest(userId, eventId, reqId);
        log.info("Заявка подтверждена");
        return request;
    }

    /**
     * Отклонение чужой заявки на участие в событии текущего пользователя.
     *
     * @param userId  id текущего пользователя
     * @param eventId id события текущего пользователя
     * @param reqId   id заявки, которую подтверждает текущий пользователь
     * @return возвращает отклоненный запрос на участие в событии {@link ParticipationRequestDto}
     */
    @PatchMapping("/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequest(@PathVariable Long userId,
                                                 @PathVariable Long eventId,
                                                 @PathVariable Long reqId) {
        var request = requestService.rejectRequest(userId, eventId, reqId);
        log.info("Заявка отклонена");
        return request;
    }
}
