package ewm.api.privateApiController.userPrivateController;

import ewm.dto.ParticipationRequestDto;
import ewm.service.interfaces.RequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Закрытый контроллер запросов ({@link ewm.model.Request})
 * для пользователя
 *
 * @author safiulinrm
 * @see ewm.api.publicApi.publicController.EventController
 */
@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    /**
     * Сервис для работы с запросами на участие в событии {@link RequestService}
     */
    private final RequestService requestService;

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     *
     * @param userId id текущего пользователя
     * @return возвращает найденные запросы на участие {@link List {@link ParticipationRequestDto}}
     */
    @GetMapping
    public List<ParticipationRequestDto> getRequestsOfUser(@PathVariable Long userId) {
        var requests = requestService.getRequestsOfUser(userId);
        log.info("Найдены запросы на участие");
        return requests;
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии.
     * Обратите внимание:
     * нельзя добавить повторный запрос
     * инициатор события не может добавить запрос на участие в своём событии
     * нельзя участвовать в неопубликованном событии
     * если у события достигнут лимит запросов на участие - необходимо вернуть ошибку
     * если для события отключена пре-модерация запросов на участие, то запрос должен
     * автоматически перейти в состояние подтвержденного
     *
     * @param userId  id текущего пользователя
     * @param eventId id события
     * @return возвращает добавленный запрос {@link ParticipationRequestDto}
     */
    @PostMapping
    public ParticipationRequestDto addRequest(@PathVariable Long userId,
                                              @RequestParam Long eventId) {
        var request = requestService.addRequest(userId, eventId);
        log.info("Заявка создана");
        return request;
    }

    /**
     * Отмена своего запроса на участие в событии
     *
     * @param userId    id текущего пользователя
     * @param requestId id запроса на участие
     * @return возвращает отмененный запрос {@link ParticipationRequestDto}
     */
    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelRequest(@PathVariable Long userId,
                                                 @PathVariable Long requestId) {
        var request = requestService.cancelRequest(userId, requestId);
        log.info("Заявка отменена");
        return request;
    }
}
