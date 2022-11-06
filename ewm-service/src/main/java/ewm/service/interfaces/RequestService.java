package ewm.service.interfaces;

import ewm.dto.ParticipationRequestDto;

import java.util.List;

/**
 * Интерфейс сервиса обработки запросов по запросам на участие в событие
 */
public interface RequestService {
    /**
     * Получить запросы на участие в определенном событии
     *
     * @param userId  id инициатора события
     * @param eventId id cобытия
     * @return список запросов {@link List {@link ParticipationRequestDto}}
     */
    List<ParticipationRequestDto> getRequestsOfEvent(Long userId, Long eventId);

    /**
     * Подтверждение чужой заявки на участие в событии
     *
     * @param userId  id инициатора события
     * @param eventId id события
     * @param reqId id запроса на участие
     * @return Подтвержденный запрос {@link ParticipationRequestDto}
     */
    ParticipationRequestDto confirmRequest(Long userId, Long eventId, Long reqId);

    /**
     * Отклонение чужой заявки на участие в событии
     *
     * @param userId  id инициатора события
     * @param eventId id события
     * @param reqId   id запроса на участие
     * @return отклоненный запрос {@link ParticipationRequestDto}
     */
    ParticipationRequestDto rejectRequest(Long userId, Long eventId, Long reqId);

    /**
     * Получение всех запросов пользователя на участие в событиях
     *
     * @param userId id пользователя
     * @return список запросов {@link List {@link ParticipationRequestDto}}
     */
    List<ParticipationRequestDto> getRequestsOfUser(Long userId);

    /**
     * Подача запроса на участие в событии
     *
     * @param userId  id участника
     * @param eventId id события
     * @return сохраненный запрос {@link ParticipationRequestDto}
     */
    ParticipationRequestDto addRequest(Long userId, Long eventId);

    /**
     * Отмена пользователем его запроса на участие в событии
     *
     * @param userId    id участника
     * @param requestId id запроса на участие
     * @return отмененный запрос на участие {@link ParticipationRequestDto}
     */
    ParticipationRequestDto cancelRequest(long userId, long requestId);
}
