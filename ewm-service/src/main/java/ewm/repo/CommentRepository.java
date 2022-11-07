package ewm.repo;

import ewm.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс репозитория для сохранения комментариев {@link Comment}
 */
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
