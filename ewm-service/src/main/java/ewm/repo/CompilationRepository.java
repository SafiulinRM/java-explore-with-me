package ewm.repo;

import ewm.model.Compilation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс репозитория для сохранения {@link Compilation}
 */
public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    /**
     * Получение Page<Compilation> закрепленных или нет подборок событий на главной странице
     *
     * @param pinned   закреплены или нет на главной странице
     * @param pageable {@link Pageable}
     * @return Page of {@link Compilation}
     */
    Page<Compilation> findByPinned(Boolean pinned, Pageable pageable);
}
