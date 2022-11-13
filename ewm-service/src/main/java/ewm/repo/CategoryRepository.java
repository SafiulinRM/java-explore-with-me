package ewm.repo;

import ewm.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс репозитория для сохранения категории {@link Category}
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
