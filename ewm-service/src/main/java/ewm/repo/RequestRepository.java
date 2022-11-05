package ewm.repo;

import ewm.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Интерфейс репозитория Запросов на участие в событиях {@link Request}
 */
public interface RequestRepository extends JpaRepository<Request, Long> {
    /**
     * Плучение запросов по id события
     *
     * @param eventId id события
     * @return List of {@link Request}
     */
    List<Request> findByEventId(long eventId);

    /**
     * Получение запроса на участие
     *
     * @param requesterId id участника
     * @param eventId     id события
     * @return {@link Request}
     */
    Request findByRequesterIdAndEventId(long requesterId, long eventId);

    /**
     * Получение запросов на участие определенного пользователя
     *
     * @param userId id участника
     * @return {@link Request}
     */
    List<Request> findByRequesterId(long userId);
}
