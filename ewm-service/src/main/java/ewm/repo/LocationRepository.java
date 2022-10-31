package ewm.repo;

import ewm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByLatAndLon(float lat, float lon);
}
