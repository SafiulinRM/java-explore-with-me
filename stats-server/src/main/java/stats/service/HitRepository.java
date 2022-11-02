package stats.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import stats.model.Hit;
import stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Long> {
    @Query("select new stats.model.ViewStats(e.app, e.uri, count(distinct e.ip)) " +
            "from Hit e " +
            "where e.timestamp between :start and :end and e.uri in (:uris) " +
            "group by e.app, e.uri")
    List<ViewStats> getStatsUnique(LocalDateTime start,
                                   LocalDateTime end,
                                   List<String> uris);

    @Query("select new stats.model.ViewStats(e.app, e.uri, count(e.ip)) " +
            "from Hit e " +
            "where e.timestamp between :start and :end and e.uri in (:uris) " +
            "group by e.app, e.uri")
    List<ViewStats> getStats(LocalDateTime start,
                             LocalDateTime end,
                             List<String> uris);
}
