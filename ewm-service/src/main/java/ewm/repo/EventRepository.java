package ewm.repo;

import ewm.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * Интерфейс репозитория событий {@link Event}
 */
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    /**
     * Получение списка событий по id инициатора
     *
     * @param userId   id инициатора
     * @param pageable {@link Pageable}
     * @return Page of {@link Event}
     */
    Page<Event> findByInitiatorId(Long userId, Pageable pageable);
}