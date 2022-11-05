package ewm.repo;

import ewm.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Интерфейс репозитория Юзеров {@link User}
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Получение списка Юзеров по списку id
     *
     * @param ids      список id
     * @param pageable {@link Pageable}
     * @return Page of {@link User}
     */
    @Query("select u from User u " +
            "where u.id in ?1")
    Page<User> getUsersByIds(List<Long> ids, Pageable pageable);
}
