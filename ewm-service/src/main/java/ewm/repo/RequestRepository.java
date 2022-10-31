package ewm.repo;

import ewm.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByEventId(long eventId);

    Request findByRequesterIdAndEventId(long requesterId, long eventId);

    List<Request> findByRequesterId(long userId);
}
