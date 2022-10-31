package stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import stats.model.Hit;
import stats.model.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StatsService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final HitRepository hitRepository;

    @Transactional
    public void postHit(Hit hit) {
        hitRepository.save(hit);
    }

    public List<ViewStats> getStats(String start,
                                    String end,
                                    List<String> uris,
                                    Boolean unique) {
        List<ViewStats> stats;
        LocalDateTime formattedStart = LocalDateTime.parse(start, FORMATTER);
        LocalDateTime formattedEnd = LocalDateTime.parse(end, FORMATTER);
        if (unique.equals(true)) {
            stats = hitRepository.getStatsUnique(formattedStart, formattedEnd, uris);
        } else {
            stats = hitRepository.getStats(formattedStart, formattedEnd, uris);
        }
        return stats;
    }
}