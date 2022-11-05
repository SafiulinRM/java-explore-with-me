package ewm.service.interfaces;

import ewm.dto.event.*;
import ewm.util.filter.EventAdminFilter;
import ewm.util.filter.EventUserFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Интерфейс сервиса обработки запросов по Событиям
 */
public interface EventService {
    /**
     * Получить события с помощью фильтрации
     *
     * @param filter  класс включающий множество параметров фильтра {@link EventUserFilter}
     * @param request данные о запросе событий {@link HttpServletRequest}
     * @return {@link List {@link EventShortDto}}
     */
    List<EventShortDto> getEvents(EventUserFilter filter, HttpServletRequest request);

    /**
     * Получить событие
     *
     * @param id      id события
     * @param request данные о запросе событий {@link HttpServletRequest}
     * @return {@link EventFullDto}
     */
    EventFullDto getEvent(Long id, HttpServletRequest request);

    /**
     * Получить все события созданные инициатором
     *
     * @param userId id инициатора событий
     * @param from   количество элементов, которые нужно пропустить для формирования текущего набора
     *               Default value : 0
     * @param size   количество элементов в наборе
     *               Default value : 10
     * @return {@link List {@link EventFullDto}}
     */
    List<EventShortDto> getEventsOfUser(Long userId, int from, int size);

    /**
     * Обновить событие инициатором
     *
     * @param userId      id инициатора события
     * @param updateEvent данные обновленного инициатором события {@link UpdateEventRequest}
     * @return {@link EventFullDto}
     */
    EventFullDto updateEvent(Long userId, UpdateEventRequest updateEvent);

    /**
     * Создать событие
     *
     * @param userId      id инициатора события
     * @param newEventDto данные нового события {@link NewEventDto}
     * @return {@link EventFullDto}
     */
    EventFullDto addEvent(Long userId, NewEventDto newEventDto);

    /**
     * Получить событие инициатором
     *
     * @param userId  id инициатора события
     * @param eventId id искомого события
     * @return {@link EventFullDto}
     */
    EventFullDto getEvent(Long userId, Long eventId);

    /**
     * Отменить событие пользователем
     *
     * @param userId  id инициатора события
     * @param eventId id события которое хотят отменить
     * @return {@link EventFullDto}
     */
    EventFullDto cancelEvent(Long userId, Long eventId);

    /**
     * Получить список событий по параметрам для админа
     *
     * @param filter {@link EventAdminFilter}
     * @return List of {@link EventFullDto}
     */
    List<EventFullDto> getEvents(EventAdminFilter filter);

    /**
     * Обновить событие администратором
     *
     * @param eventId     id события
     * @param updateEvent {@link AdminUpdateEventRequest}
     * @return {@link EventFullDto}
     */
    EventFullDto putEvent(Long eventId, AdminUpdateEventRequest updateEvent);

    /**
     * Опубликовать событие
     *
     * @param eventId id события
     * @return {@link EventFullDto}
     */
    EventFullDto publishEvent(Long eventId);

    /**
     * Отклонить событие
     *
     * @param eventId id события
     * @return {@link EventFullDto}
     */
    EventFullDto rejectEvent(Long eventId);
}
