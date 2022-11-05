package ewm.repo;

import ewm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Интерфейс репозитория локаций {@link Location}
 */
public interface LocationRepository extends JpaRepository<Location, Long> {
    /**
     * Найти локацию по ширине и долготе
     *
     * @param lat широта
     * @param lon долгота
     * @return {@link Location}
     */
    Location findByLatAndLon(float lat, float lon);
}
